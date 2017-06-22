package chaneko.manage.lambda;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class DateUtil {

	public static boolean isDate(String text) {

		if (Stream.of("おととい", "一昨日", "きのう", "昨日", "きょう", "今日", "あした", "明日", "あさって", "明後日", "しあさって", "明々後日", "明明後日")
				.anyMatch(match -> text.contains(match))) {
			return true;
		}

		try {
			LocalDate.parse(text, DateTimeFormatter.ofPattern("uuuu/MM/dd"));
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static boolean isTime(String text) {

		try {
			LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm"));
		} catch (Exception e) {
			return false;
		}

		return true;
	}

}
