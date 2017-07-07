package chaneko.manage.lambda;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;

import chaneko.manage.lambda.dynamo.InsertDAO;
import chaneko.manage.lambda.dynamo.Item;
import chaneko.manage.lambda.dynamo.SelectDAO;
import chaneko.manage.lambda.dynamo.UpdateDAO;

public class App {

    private static final String STAGE_TABLE = "chaneko_stage";

    private static final String STAGE_CONTENT_TABLE = "chaneko_stage_content";

    private static final String MESSAGE_TABLE = "chaneko_stage_text";

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

        String stage = getStage(event);

        List<StageContent> stageContents = getStageContent(stage);

        return stageContents.stream().filter(content -> matchContent(content, event))
                .map(content -> exec(content, event)).findFirst().orElseGet(() -> {
                    return Result.of("System Error!");
                });
    }

    private String getStage(Event event) {

        Supplier<Item> getStage = () -> SelectDAO.execute(
            builder -> builder.tableName(STAGE_TABLE).key("user_name", event.getUser_name()));

        Item stage = getStage.get();

        if (stage.isNull()) {

            // 新規ユーザの場合、ユーザを作成する
            InsertDAO.execute(
                builder -> builder.tableName(STAGE_TABLE).addItem("user_name", event.getUser_name())
                        .addItem("stage", "main").addItem("mode", "text_normal")
                        .addItem("last_access", String.valueOf(System.currentTimeMillis())));
            stage = getStage.get();
        }

        UpdateDAO.execute(
            builder -> builder.tableName(STAGE_TABLE).key("user_name", event.getUser_name())
                    .addItem("last_access", String.valueOf(System.currentTimeMillis())));

        return stage.get("stage");
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

        if (content.getMode() != null) {
            UpdateDAO.execute(builder -> builder.tableName(STAGE_TABLE)
                    .key("user_name", event.getUser_name()).addItem("mode", content.getMode()));
        }

        String mode = SelectDAO.execute(
            builder -> builder.tableName(STAGE_TABLE).key("user_name", event.getUser_name()))
                .get("mode");
        UpdateDAO.execute(builder -> builder.tableName(STAGE_TABLE)
                .key("user_name", event.getUser_name()).addItem("stage", content.getTo()));

        if (content.getText() == null) {
            return Result.of(null);
        }

        return Result.of(content.getText().get(mode));

    }

    private List<StageContent> getStageContent(String stage) {

        Item item = SelectDAO
                .execute(builder -> builder.tableName(STAGE_CONTENT_TABLE).key("stage", stage));

        return item.getList("contents").stream().map(this::convert)
                .sorted(Comparator.comparing(StageContent::getSort)).collect(Collectors.toList());

    }

    private StageContent convert(AttributeValue val) {

        Map<String, AttributeValue> map = val.getM();

        Map<String, String> collect = map.entrySet().stream().collect(
            Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().getS()));

        return new StageContent().setMatch(collect.get("match")).setMode(collect.get("mode"))
                .setSort(Integer.valueOf(collect.get("sort"))).setText(getText(collect.get("text")))
                .setTo(collect.get("to")).setType(collect.get("type"));
    }

    private Map<String, String> getText(String id) {

        if (id == null) {
            return null;
        }

        return SelectDAO.execute(builder -> builder.tableName(MESSAGE_TABLE).key("message_id", id))
                .getToMap();
    }

}
