package chaneko.manage.lambda.dynamo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import chaneko.manage.lambda.StageContent;
import chaneko.manage.lambda.StaticLogger;

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

    public String getMode(String name) {

        Map<String, AttributeValue> item = manager.getItem("chaneko_stage", "user_name", name);

        if (item.get("mode") == null || "".equals(item.get("mode").getS())) {
            manager.updateItem("chaneko_stage", "user_name", name, "mode", "text_normal");
            return "text_normal";
        }

        return item.get("mode").getS();
    }

    public void updateStage(String name, String stageName) {
        manager.updateItem("chaneko_stage", "user_name", name, "stage", stageName);
    }

    public void insertStage(String name) {
        manager.insertItem("chaneko_stage", "user_name", name, "stage", "main");
    }

    public List<StageContent> getStageContent(String stage) {

        Map<String, AttributeValue> item = manager.getItem("chaneko_stage_content", "stage", stage);

        StaticLogger.log(item.toString());
        StaticLogger.log(item.get("contents").toString());
        return item.get("contents").getL().stream().map(this::convert)
                .sorted(Comparator.comparing(StageContent::getSort)).collect(Collectors.toList());

    }

    private StageContent convert(AttributeValue val) {

        Map<String, AttributeValue> map = val.getM();

        return new StageContent().setMatch(get(map.get("match"), AttributeValue::getS))
                .setSort(get(map.get("sort"), v -> Integer.parseInt(v.getS())))
                .setText_honorific(get(map.get("text_honorific"), AttributeValue::getS))
                .setText_neko(get(map.get("text_neko"), AttributeValue::getS))
                .setText_normal(get(map.get("text_normal"), AttributeValue::getS))
                .setTo(get(map.get("to"), AttributeValue::getS))
                .setTable(get(map.get("table"), AttributeValue::getS))
                .setType(get(map.get("type"), AttributeValue::getS))
                .setDb(get(map.get("db"), AttributeValue::getS))
                .setKey(get(map.get("key"), AttributeValue::getS))
                .setKeyName(get(map.get("keyname"), AttributeValue::getS))
                .setUpdateName(get(map.get("updatename"), AttributeValue::getS))
                .setUpdateVal(get(map.get("updateval"), AttributeValue::getS)).update();
    }

    private <T> T get(AttributeValue val, Function<AttributeValue, T> func) {
        return Optional.ofNullable(val).map(func).orElse(null);
    }

}
