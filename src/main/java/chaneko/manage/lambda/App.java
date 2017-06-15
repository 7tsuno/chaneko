package chaneko.manage.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import chaneko.manage.lambda.stage.StageManager;

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

        return new StageManager().callStage(event);
    }
}
