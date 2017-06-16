package chaneko.manage.lambda;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;

import chaneko.manage.lambda.dynamo.DAO;
import chaneko.manage.lambda.dynamo.DynamoManager;

public class App {

    /**
     * ハンドラ.
     * 
     * @param event イベント
     * @param context コンテキスト
     * @return 返却結果
     */
    public Result handler(Event event, Context context) {

        StaticLogger.set(context);
        StaticLogger.log("start");

        event.setText(event.getText().replaceFirst("@chaneko", "").trim());

        return callStage(event);
    }

    private Result callStage(Event event) {

        DAO dao = new DAO();
        String stage = dao.getStage(event.getUser_name());
        String mode = dao.getMode(event.getUser_name());

        List<StageContent> stageContents = dao.getStageContent(stage);

        for (StageContent content : stageContents) {

            StaticLogger.log(content.toString());

            if (matchContent(content, event)) {

                if (content.getDb() != null && "update".equals(content.getDb())) {

                    DynamoManager manager = new DynamoManager();
                    manager.updateItem(content.getTable(), content.getKeyName(),
                        getKey(content.getKey(), event), content.getUpdateName(),
                        content.getUpdateVal());

                }

                dao.updateStage(event.getUser_name(), content.getTo());

                return Result.of(content.getText().get(mode));
            }

        }

        dao.updateStage(event.getUser_name(), "main");

        return Result.of("システムエラーです。管理者に報告されます @nakayama");
    }

    private String getKey(String key, Event event) {
        if ("_user".equals(key)) {
            return event.getUser_name();
        }
        return key;
    }

    public boolean matchContent(StageContent content, Event event) {

        String type = content.getType();

        if ("else".equals(type)) {
            return true;
        }
        if ("contains".equals(type)) {
            return event.getText().contains(content.getMatch());
        }
        if ("equals".equals(type)) {
            return event.getText().equals(content.getMatch());
        }

        return false;
    }
}
