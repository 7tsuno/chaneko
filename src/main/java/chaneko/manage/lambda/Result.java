package chaneko.manage.lambda;

import lombok.Data;

@Data
public class Result {

	private String text;
	private String link_names = "1";

	public static Result of(String text) {
		Result result = new Result();
		result.text = text;
		return result;
	}
}
