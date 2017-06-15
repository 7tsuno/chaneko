package chaneko.manage.lambda.stage;

import chaneko.manage.lambda.Result;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StageResult {

    private Result result;

    private String nextStage;

}
