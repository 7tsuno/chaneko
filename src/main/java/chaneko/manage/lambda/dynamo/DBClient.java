package chaneko.manage.lambda.dynamo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DBClient {

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_2).build();

    private static DBClient dbClient = new DBClient();

    private DBClient() {
    }

    public static AmazonDynamoDB get() {
        return dbClient.client;
    }

}
