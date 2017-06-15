package chaneko.manage.lambda.dynamo;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

public class DynamoManager {

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_2).build();

    public Map<String, AttributeValue> getItem(String tableName, String keyName, String keyValue) {

        Map<String, AttributeValue> map = new HashMap<>();
        map.put(keyName, new AttributeValue(keyValue));

        GetItemResult item = client.getItem(tableName, map);

        return item.getItem();
    }

    public void updateItem(String tableName, String keyName, String keyValue, String updateColumn,
            String updateValue) {

        Map<String, AttributeValue> map = new HashMap<>();
        map.put(keyName, new AttributeValue(keyValue));

        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put(updateColumn,
            new AttributeValueUpdate(new AttributeValue(updateValue), AttributeAction.PUT));
        client.updateItem(tableName, map, updates);
    }

    public void insertItem(String tableName, String keyName, String keyValue, String insertColumn,
            String insertValue) {
        Map<String, AttributeValue> map = new HashMap<>();
        map.put(keyName, new AttributeValue(keyValue));
        map.put(insertColumn, new AttributeValue(insertValue));
        client.putItem(tableName, map);
    }

}
