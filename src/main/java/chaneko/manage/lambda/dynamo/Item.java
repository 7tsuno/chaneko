package chaneko.manage.lambda.dynamo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class Item {

	private Map<String, AttributeValue> map;

	protected Item(Map<String, AttributeValue> map) {
		this.map = map;
	}

	public Map<String, String> getToMap() {
		return map.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().getS()));
	}

	public String get(String columnName) {
		return map.get(columnName).getS();
	}

	public List<AttributeValue> getList(String columnName) {
		return map.get(columnName).getL();
	}

	public boolean isNull() {
		return map == null;
	}

}
