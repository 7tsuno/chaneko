package chaneko.manage.lambda.dynamo;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

public class SelectDAO {

    private String tableName;

    private String keyName;

    private String keyValue;

    private SelectDAO() {
    }

    public static SelectDAO of(String tableName) {
        SelectDAO builder = new SelectDAO();
        builder.tableName = tableName;
        return builder;
    }

    public SelectDAO setKey(String keyName, String keyValue) {
        this.keyName = keyName;
        this.keyValue = keyValue;
        return this;
    }

    public Map<String, AttributeValue> select() {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put(keyName, new AttributeValue(keyValue));
        GetItemResult item = DBClient.get().getItem(tableName, key);
        return item.getItem();
    }
}
