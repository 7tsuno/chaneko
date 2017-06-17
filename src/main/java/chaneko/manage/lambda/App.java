package chaneko.manage.lambda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;

import chaneko.manage.lambda.dynamo.DAO;
import chaneko.manage.lambda.dynamo.DynamoManager;

public class App {

	/**
	 * 独自処理.
	 */
	private Map<String, BiConsumer<Event, StageContent>> function;

	/**
	 * ハンドラ.
	 * 
	 * @param event
	 *            イベント
	 * @param context
	 *            コンテキスト
	 * @return 返却結果
	 */
	public Result handler(Event event, Context context) {

		event.setText(event.getText().replaceFirst("@chaneko", "").trim());

		return callStage(event);
	}

	/**
	 * ステージ呼び出し. <br>
	 * ステージに付随する内容を呼び出し、マッチする実行内容を実行する.
	 * 
	 * @param event
	 * @return
	 */
	private Result callStage(Event event) {

		DAO dao = new DAO();
		String stage = dao.getStage(event.getUser_name());

		List<StageContent> stageContents = dao.getStageContent(stage);

		return stageContents.stream().filter(content -> matchContent(content, event))
				.map(content -> exec(content, event)).findFirst().orElseGet(() -> {
					dao.updateStage(event.getUser_name(), "main");
					return Result.of("システムエラーです。管理者に報告されます @nakayama");
				});
	}

	/**
	 * 取得したステージ内容が条件に一致するかを判定する.<br>
	 * 
	 * @param content
	 *            ステージ内容
	 * @param event
	 *            入力情報
	 * @return true : 一致する / false : 一致しない
	 */
	public boolean matchContent(StageContent content, Event event) {

		String type = content.getType();

		if ("else".equals(type)) {
			return true;
		}

		if ("contains".equals(type)) {
			return Stream.of(content.getMatch().split(",")).anyMatch(match -> event.getText().contains(match));
		}

		if ("equals".equals(type)) {
			return Stream.of(content.getMatch().split(",")).anyMatch(match -> event.getText().equals(match));
		}

		return false;
	}

	private Result exec(StageContent content, Event event) {

		// 独自処理がある場合、実行する
		if (content.getExecute() != null) {
			function.get(content.getExecute()).accept(event, content);
			
		}

		DAO dao = new DAO();
		String mode = dao.getMode(event.getUser_name());
		dao.updateStage(event.getUser_name(), content.getTo());
		return Result.of(content.getText().get(mode));

	}

	/**
	 * コンストラクタ.<br>
	 * Durationに含まれない初期化処理を実行する.<br>
	 * 単純ステージ遷移ではない独自処理をここに記述する.
	 */
	public App() {

		function = new HashMap<>();

		// DB更新処理
		function.put("update", (event, content) -> {
			DynamoManager manager = new DynamoManager();
			manager.updateItem(content.getTable(), content.getKeyName(), getKey(content.getKey(), event),
					content.getUpdateName(), content.getUpdateVal());
		});

	}

	private String getKey(String key, Event event) {
		if ("_user".equals(key)) {
			return event.getUser_name();
		}
		return key;
	}
}
