package chaneko.manage.lambda.stage.schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import chaneko.manage.lambda.Event;
import chaneko.manage.lambda.Result;
import chaneko.manage.lambda.stage.Stage;
import chaneko.manage.lambda.stage.StageResult;

public class ScheduleStartTimeStage implements Stage {

    @Override
    public StageResult call(Event event) {

        if (event.getText().contains("やめる")) {
            return new StageResult().setResult(Result.of("はーい！")).setNextStage("main");
        } else {

            try {
                LocalTime time = LocalTime.parse(event.getText(),
                    DateTimeFormatter.ofPattern("HHmm"));
                return new StageResult().setResult(Result.of("分かった！" + time + "だね！予定のタイトルはどうする？"))
                        .setNextStage("1-3");
            } catch (DateTimeParseException e) {
                return new StageResult().setResult(Result.of("HHmmで教えて！")).setNextStage("1-2");
            }

        }
    }

}
