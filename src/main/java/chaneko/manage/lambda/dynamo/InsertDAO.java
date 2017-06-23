package chaneko.manage.lambda.dynamo;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class InsertDAO {

    private String tableName;

    private Map<String, AttributeValue> items;

    private InsertDAO() {
    }

    public static InsertDAO of(String tableName) {
        InsertDAO builder = new InsertDAO();
        builder.tableName = tableName;
        builder.items = new HashMap<>();
        return builder;
    }

    public InsertDAO addInsertItem(String insertColumnName, String insertColumnValue) {
        items.put(insertColumnName, new AttributeValue(insertColumnValue));
        return this;
    }

    public void insert() {
        DBClient.get().putItem(tableName, items);
    }
}
