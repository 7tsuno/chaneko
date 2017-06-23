package chaneko.manage.lambda.dynamo;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

public class UpdateDAO {

    private String tableName;

    private String keyName;

    private String keyValue;

    private Map<String, AttributeValueUpdate> items;

    private UpdateDAO() {
    }

    public static UpdateDAO of(String tableName) {
        UpdateDAO builder = new UpdateDAO();
        builder.tableName = tableName;
        builder.items = new HashMap<>();
        return builder;
    }

    public UpdateDAO setKey(String keyName, String keyValue) {
        this.keyName = keyName;
        this.keyValue = keyValue;
        return this;
    }

    public UpdateDAO addUpdateItem(String updateColumnName, String updateColumnValue) {
        items.put(updateColumnName,
            new AttributeValueUpdate(new AttributeValue(updateColumnValue), AttributeAction.PUT));
        return this;
    }

    public void update() {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(keyName, new AttributeValue(keyValue));
        DBClient.get().updateItem(tableName, key, items);
    }
}
