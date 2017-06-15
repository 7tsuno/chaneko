package chaneko.manage.lambda.stage;

import chaneko.manage.lambda.Event;

public interface Stage {

    StageResult call(Event event);

}
