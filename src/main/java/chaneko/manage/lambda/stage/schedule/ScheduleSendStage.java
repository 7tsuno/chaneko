package chaneko.manage.lambda.stage.schedule;

import chaneko.manage.lambda.Event;
import chaneko.manage.lambda.Result;
import chaneko.manage.lambda.stage.Stage;
import chaneko.manage.lambda.stage.StageResult;

public class ScheduleSendStage implements Stage {

    @Override
    public StageResult call(Event event) {

        if (event.getText().contains("やめる")) {
            return new StageResult().setResult(Result.of("はーい！")).setNextStage("main");
        } else if (event.getText().contains("うん") || event.getText().contains("はい")
                || event.getText().contains("YES")) {
            return new StageResult().setResult(Result.of("まだ実装中でーす！")).setNextStage("main");
        } else {
            return new StageResult().setResult(Result.of("ちょっと何言ってるかわかんないや！")).setNextStage("1-5");
        }
    }

}
