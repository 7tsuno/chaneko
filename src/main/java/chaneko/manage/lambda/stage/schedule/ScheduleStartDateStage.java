package chaneko.manage.lambda.stage.schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import chaneko.manage.lambda.Event;
import chaneko.manage.lambda.Result;
import chaneko.manage.lambda.stage.Stage;
import chaneko.manage.lambda.stage.StageResult;

public class ScheduleStartDateStage implements Stage {

    @Override
    public StageResult call(Event event) {

        if (event.getText().contains("やめる")) {
            return new StageResult().setResult(Result.of("はーい！")).setNextStage("main");
        } else if (event.getText().equals("今日")) {
            return new StageResult()
                    .setResult(Result.of("分かった！" + LocalDate.now() + "だね！何時に予定を入れる？"))
                    .setNextStage("1-2");
        } else if (event.getText().equals("明日")) {
            return new StageResult()
                    .setResult(Result.of("分かった！" + LocalDate.now().plusDays(1) + "だね！何時に予定を入れる？"))
                    .setNextStage("1-2");
        } else if (event.getText().equals("明後日")) {
            return new StageResult()
                    .setResult(Result.of("分かった！" + LocalDate.now().plusDays(2) + "だね！何時に予定を入れる？"))
                    .setNextStage("1-2");
        } else {

            try {
                LocalDate localDate = LocalDate.parse(event.getText(),
                    DateTimeFormatter.ofPattern("uuuuMMdd"));
                return new StageResult().setResult(Result.of("分かった！" + localDate + "だね！何時に予定を入れる？"))
                        .setNextStage("1-2");
            } catch (DateTimeParseException e) {
                return new StageResult().setResult(Result.of("yyyyMMddで教えて！")).setNextStage("1-1");
            }

        }
    }

}
