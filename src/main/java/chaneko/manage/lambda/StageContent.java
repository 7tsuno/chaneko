package chaneko.manage.lambda;

import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StageContent {

	private String match;

	private int sort;

	private Map<String, String> text;

	private String to;

	private String type;

	private String mode;
}
