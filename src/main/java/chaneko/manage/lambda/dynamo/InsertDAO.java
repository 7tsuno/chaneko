package chaneko.manage.lambda.dynamo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class InsertDAO {

	private String tableName;

	private Map<String, AttributeValue> items;

	private InsertDAO() {
	}

	public static void execute(Consumer<InsertDAO> builder) {
		InsertDAO dao = new InsertDAO();
		dao.items = new HashMap<>();
		builder.accept(dao);
		DBClient.get().putItem(dao.tableName, dao.items);

	}

	public InsertDAO tableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public InsertDAO addItem(String insertColumnName, String insertColumnValue) {
		items.put(insertColumnName, new AttributeValue(insertColumnValue));
		return this;
	}

	public void insert() {
		DBClient.get().putItem(tableName, items);
	}
}
