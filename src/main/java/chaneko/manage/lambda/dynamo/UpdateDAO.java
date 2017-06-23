package chaneko.manage.lambda.dynamo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

	public static void execute(Consumer<UpdateDAO> builder) {
		UpdateDAO dao = new UpdateDAO();
		dao.items = new HashMap<>();
		builder.accept(dao);
		Map<String, AttributeValue> key = new HashMap<>();
		key.put(dao.keyName, new AttributeValue(dao.keyValue));
		DBClient.get().updateItem(dao.tableName, key, dao.items);

	}

	public UpdateDAO tableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public UpdateDAO key(String keyName, String keyValue) {
		this.keyName = keyName;
		this.keyValue = keyValue;
		return this;
	}

	public UpdateDAO addItem(String updateColumnName, String updateColumnValue) {
		items.put(updateColumnName,
				new AttributeValueUpdate(new AttributeValue(updateColumnValue), AttributeAction.PUT));
		return this;
	}

}
