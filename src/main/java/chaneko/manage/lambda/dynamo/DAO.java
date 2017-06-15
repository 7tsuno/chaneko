package chaneko.manage.lambda.dynamo;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class DAO {

    private DynamoManager manager = new DynamoManager();

    public String getStage(String name) {

        Map<String, AttributeValue> item = manager.getItem("chaneko_stage", "user_name", name);

        if (item == null) {
            insertStage(name);
            return "main";
        }

        return item.get("stage").getS();
    }

    public void updateStage(String name, String stageName) {
        manager.updateItem("chaneko_stage", "user_name", name, "stage", stageName);
    }

    public void insertStage(String name) {
        manager.insertItem("chaneko_stage", "user_name", name, "stage", "main");
    }
}
