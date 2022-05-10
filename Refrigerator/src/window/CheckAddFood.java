package window;

import window.database.Connect;
import window.general.Input;

/**
 * 食品追加確認画面...12
 */
public class CheckAddFood extends Default {
	private static String name = "食品追加確認画面";

	public CheckAddFood() {
		super(name);
	}

	window.general.Input input = new Input();

	private final String[] menus = { "類似した食品を確認", "新規食品として追加", "前の画面に戻る", "メニューに戻る" };
	private final int[] menunumber = { 0, 0, 4, 0 };
	int userselect = 0;

	/**
	 * 実行部
	 */
	@Override
	public int process() {
		// DB接続
		window.database.Connect con = new Connect();
		String foodname = con.getOnetimeFoodname();
		String[][] foods = con.getFood(foodname);
		String[] passfoods = new String[foods.length + 1];
		passfoods[foods.length] = "前の画面に戻る";
		do {
			// ユーザー選択
			userselect = input.select(menus);
			if (userselect == 0) {
				// 該当食品があれば一覧表示
				if (foods.length != 0) {
					for (int i = 0; i < foods.length; i++) {
						System.out.print("0:食品名：" + foods[i][1]);
						System.out.print(" | 期限：" + foods[i][6]);
						System.out.print(" | 在庫：" + foods[i][2]);
						System.out.println(" | 備考:" + foods[i][5]);
						passfoods[i] = foods[i][1];
					}
					System.out.println("在庫数を追加するか前の画面に戻ってください。");
				} else {
					System.out.println("該当する食品が存在しませんでした。");
				}
				int result = input.select(passfoods);
				if (result == foods.length) {
					System.out.println("処理を中断しました");
					continue;
				} else {
					// 在庫追加処理
					if (con.addStock(Integer.parseInt(foods[result][0]))) {
						System.out.println("在庫を追加しました。");
					} else {
						con.deleteOnetime();
						System.out.println("エラーが起こりました。最初からやり直してください。");
					}
					break;
				}
			} else if (userselect == 1) {
				// 新規追加処理
				if (con.addFood()) {
					System.out.println("新規食品として追加しました。システムメニューに戻ります。");
				} else {
					con.deleteOnetime();
					System.out.println("エラーが起きました。恐れ入りますが、最初からやり直してください。");
				}
				break;
			} else {
				// 一時データ削除
				con.deleteOnetime();
				break;
			}
		} while (true);
		return menunumber[userselect];
	}

}