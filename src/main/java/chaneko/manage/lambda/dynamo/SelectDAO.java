package chaneko.manage.lambda.dynamo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

public class SelectDAO {

	private String tableName;

	private String keyName;

	private String keyValue;

	private SelectDAO() {
	}

	public static Item execute(Consumer<SelectDAO> builder) {
		SelectDAO dao = new SelectDAO();
		builder.accept(dao);
		Map<String, AttributeValue> key = new HashMap<>();
		key.put(dao.keyName, new AttributeValue(dao.keyValue));
		GetItemResult item = DBClient.get().getItem(dao.tableName, key);
		return new Item(item.getItem());
	}

	public SelectDAO tableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public SelectDAO key(String keyName, String keyValue) {
		this.keyName = keyName;
		this.keyValue = keyValue;
		return this;
	}
}
