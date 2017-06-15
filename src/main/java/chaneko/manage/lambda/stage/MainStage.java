package chaneko.manage.lambda.stage;

import chaneko.manage.lambda.Event;
import chaneko.manage.lambda.Result;
import lombok.Data;

/**
 * メインステージでの処理.<br>
 * デフォルトのステージ.
 */
@Data
public class MainStage implements Stage {

    public StageResult call(Event event) {

        if (event.getText().contains("何が出来るの")) {
            return new StageResult()
                    .setResult(Result.of("今できることは、こんな感じ！" + System.lineSeparator() + "1. 予定を立てる"))
                    .setNextStage("main");
        } else if (event.getText().contains("予定を立てたい")) {
            return new StageResult().setResult(Result.of("分かった！何日に予定を入れる？")).setNextStage("1-1");
        } else if (event.getText().contains("おーい")) {
            return new StageResult().setResult(Result.of("はーい！ちゃねこに出来ることは「何が出来るの」って聞いてね！"))
                    .setNextStage("main");
        } else if (event.getText().contains("かわいい")) {
            return new StageResult().setResult(Result.of("えへへ、ありがと！")).setNextStage("main");
        } else {
            return new StageResult()
                    .setResult(Result.of("ちょっと何言ってるかわかんないや！ちゃねこに出来ることは「何が出来るの」って聞いてね！"))
                    .setNextStage("main");
        }
    }

}
