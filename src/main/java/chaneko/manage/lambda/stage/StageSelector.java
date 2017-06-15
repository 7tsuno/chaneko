package chaneko.manage.lambda.stage;

import chaneko.manage.lambda.stage.schedule.ScheduleContentStage;
import chaneko.manage.lambda.stage.schedule.ScheduleSendStage;
import chaneko.manage.lambda.stage.schedule.ScheduleStartDateStage;
import chaneko.manage.lambda.stage.schedule.ScheduleStartTimeStage;
import chaneko.manage.lambda.stage.schedule.ScheduleTitleStage;

public class StageSelector {

    public static Stage select(String stageName) {

        switch (stageName) {
            case "main":
                return new MainStage();

            case "1-1":
                return new ScheduleStartDateStage();

            case "1-2":
                return new ScheduleStartTimeStage();

            case "1-3":
                return new ScheduleTitleStage();

            case "1-4":
                return new ScheduleContentStage();

            case "1-5":
                return new ScheduleSendStage();

            default:
                throw new RuntimeException("nothing stage.");
        }

    }

}
