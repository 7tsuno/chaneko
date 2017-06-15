package chaneko.manage.lambda.stage.schedule;

import chaneko.manage.lambda.Event;
import chaneko.manage.lambda.Result;
import chaneko.manage.lambda.stage.Stage;
import chaneko.manage.lambda.stage.StageResult;

public class ScheduleContentStage implements Stage {

    @Override
    public StageResult call(Event event) {

        if (event.getText().contains("やめる")) {
            return new StageResult().setResult(Result.of("はーい！")).setNextStage("main");
        } else {
            return new StageResult()
                    .setResult(Result.of("分かった！" + event.getText() + "だね！予定を登録するよ？"))
                    .setNextStage("1-5");
        }
    }

}
