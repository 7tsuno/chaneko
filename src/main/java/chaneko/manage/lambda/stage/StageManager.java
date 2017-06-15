package chaneko.manage.lambda.stage;

import chaneko.manage.lambda.Event;
import chaneko.manage.lambda.Result;
import chaneko.manage.lambda.StaticLogger;
import chaneko.manage.lambda.dynamo.DAO;

public class StageManager {

    private DAO dao = new DAO();

    public Result callStage(Event event) {

        String stageName = dao.getStage(event.getUser_name());

        StaticLogger.log("stage : " + stageName);

        Stage stage = StageSelector.select(stageName);
        StageResult res = stage.call(event);
        dao.updateStage(event.getUser_name(), res.getNextStage());

        return res.getResult();
    }

}
