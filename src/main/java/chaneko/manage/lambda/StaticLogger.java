package chaneko.manage.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class StaticLogger {

    private static LambdaLogger lambdaLogger;

    public static void set(Context context) {
        lambdaLogger = context.getLogger();
    }

    public static void log(String str) {
        lambdaLogger.log(str + System.lineSeparator());
    }

}
