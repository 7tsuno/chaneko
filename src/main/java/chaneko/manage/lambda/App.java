package chaneko.manage.lambda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;

import chaneko.manage.lambda.dynamo.DAO;
import chaneko.manage.lambda.dynamo.DynamoManager;
import chaneko.manage.lambda.dynamo.InsertDAO;
import chaneko.manage.lambda.dynamo.SelectDAO;
import chaneko.manage.lambda.dynamo.UpdateDAO;

public class App {

    private static final String CHANEKO_STAGE = "chaneko_stage";

    /**
     * 独自処理.
     */
    private Map<String, BiFunction<Event, StageContent, String>> function;

    /**
     * ハンドラ.
     * 
     * @param event
     *            イベント
     * @param context
     *            コンテキスト
     * @return 返却結果
     */
    public Result handler(Event event, Context context) {

        event.setText(event.getText().trim());

        return callStage(event);
    }

    /**
     * ステージ呼び出し. <br>
     * ステージに付随する内容を呼び出し、マッチする実行内容を実行する.
     * 
     * @param event
     * @return
     */
    private Result callStage(Event event) {

        DAO dao = new DAO();
        String stage = getStage(event);

        List<StageContent> stageContents = dao.getStageContent(stage);

        return stageContents.stream().filter(content -> matchContent(content, event))
                .map(content -> exec(content, event)).findFirst().orElseGet(() -> {
                    dao.updateStage(event.getUser_name(), "main");
                    return Result.of("システムエラーです");
                });
    }

    private String getStage(Event event) {

        Map<String, AttributeValue> stage = SelectDAO.of(CHANEKO_STAGE)
                .setKey("user_name", event.getUser_name()).select();

        if (stage == null) {

            // 新規ユーザ
            InsertDAO.of(CHANEKO_STAGE).addInsertItem("user_name", event.getUser_name())
                    .addInsertItem("stage", "default").addInsertItem("mode", "text_normal")
                    .addInsertItem("last_access", String.valueOf(System.currentTimeMillis()))
                    .addInsertItem("exit", "0").insert();

            stage = SelectDAO.of(CHANEKO_STAGE).setKey("user_name", event.getUser_name()).select();
        }

        if (Long.valueOf(stage.get("last_access").getS()) + 300000 < System.currentTimeMillis()
                || "1".equals(stage.get("last_access").getS())) {

            // 5分以内のアクセスでは無い場合、ステージをデフォルトに修正
            UpdateDAO.of(CHANEKO_STAGE).setKey("user_name", event.getUser_name())
                    .addUpdateItem("stage", "default").addUpdateItem("exit", "0");
            stage = SelectDAO.of(CHANEKO_STAGE).setKey("user_name", event.getUser_name()).select();
        }

        UpdateDAO.of(CHANEKO_STAGE).setKey("user_name", event.getUser_name())
                .addUpdateItem("last_access", String.valueOf(System.currentTimeMillis()));

        return stage.get("stage").getS();
    }

    /**
     * 取得したステージ内容が条件に一致するかを判定する.<br>
     * 
     * @param content
     *            ステージ内容
     * @param event
     *            入力情報
     * @return true : 一致する / false : 一致しない
     */
    public boolean matchContent(StageContent content, Event event) {

        String type = content.getType();

        if ("else".equals(type)) {
            return true;
        }

        if ("contains".equals(type)) {
            return Stream.of(content.getMatch().split(","))
                    .anyMatch(match -> event.getText().contains(match));
        }

        if ("equals".equals(type)) {
            return Stream.of(content.getMatch().split(","))
                    .anyMatch(match -> event.getText().equals(match));
        }

        if ("isDate".equals(type)) {
            return DateUtil.isDate(event.getText());
        }

        if ("isTime".equals(type)) {
            return DateUtil.isTime(event.getText());
        }

        return false;
    }

    private Result exec(StageContent content, Event event) {

        // 独自処理がある場合、実行する
        String apply = "";
        if (content.getExecute() != null) {

            if ("exit".equals(content.getExecute())) {
                UpdateDAO.of(CHANEKO_STAGE).setKey("user_name", event.getUser_name())
                        .addUpdateItem("stage", "default").addUpdateItem("exit", "1");
            } else {
                apply = "\r\n" + function.get(content.getExecute()).apply(event, content);
            }
        }

        DAO dao = new DAO();
        String mode = dao.getMode(event.getUser_name());
        dao.updateStage(event.getUser_name(), content.getTo());

        if (content.getText().get(mode) == null) {
            return Result.of(null);
        }

        return Result.of("@" + event.getUser_name() + " " + content.getText().get(mode) + apply);

    }

    /**
     * コンストラクタ.<br>
     * Durationに含まれない初期化処理を実行する.<br>
     * 単純ステージ遷移ではない独自処理をここに記述する.
     */
    public App() {

        function = new HashMap<>();

        // DB更新処理
        function.put("update", (event, content) -> {
            DynamoManager manager = new DynamoManager();
            manager.updateItem(content.getTable(), content.getKeyName(),
                getKey(content.getKey(), event), content.getUpdateName(), content.getUpdateVal());
            return "";
        });

    }

    private String getKey(String key, Event event) {
        if ("_user".equals(key)) {
            return event.getUser_name();
        }
        return key;
    }
}
