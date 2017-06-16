package chaneko.manage.lambda;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StageContent {

    private String match;

    private int sort;

    private String text_honorific;

    private String text_neko;

    private String text_normal;

    private Map<String, String> text;

    private String to;

    private String type;

    private String db;

    private String table;

    private String key;

    private String keyName;

    private String updateName;

    private String updateVal;

    public StageContent update() {

        text = new HashMap<>();
        text.put("text_honorific", text_honorific);
        text.put("text_neko", text_neko);
        text.put("text_normal", text_normal);
        return this;
    }
}
