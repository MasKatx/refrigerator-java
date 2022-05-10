package window;

import window.database.Connect;
import window.general.Input;

/**
 * 食品選択確認画面...14
 */
public class CheckSelectFood extends Default {
	private static String name = "食品選択確認画面";

	public CheckSelectFood() {
		super(name);
	}

	window.general.Input input = new Input();

	private final String[] menus = { "食品を確認", "前の画面に戻る", "メニューに戻る" };
	private final int[] menunumber = { 0, 6, 0 };
	private int userselect = 0;
	private int food_id = 0;

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
		String[] connectionoflimit = { "賞味期限", "消費期限" };
		passfoods[foods.length] = "前の画面に戻る";
		do {
			// ユーザー選択
			userselect = input.select(menus);
			if (userselect == 0) {
				if (foods.length != 0) {
					for (int i = 0; i < foods.length; i++) {
						System.out.print(i + ":食品名：" + foods[i][1]);
						System.out.print(" | 期限：" + foods[i][6]);
						System.out.print(" | 在庫：" + foods[i][2]);
						System.out.println(" | 備考:" + foods[i][5]);
						passfoods[i] = foods[i][1];
					}
					System.out.println("食品を選択するか前の画面に戻ってください。");
				} else {
					System.out.println("該当する食品が存在しませんでした。");
				}
				int result = input.select(passfoods);
				if (result == foods.length) {
					// 前の画面に戻る
					System.out.println("処理を中断しました");
				} else {
					// 表示
					System.out.print("食品名：" + foods[result][1]);
					System.out.print(" | 種類：" + foods[result][3]);
					System.out.print(
							" | " + connectionoflimit[Integer.parseInt(foods[result][4])] + "：" + foods[result][6]);
					System.out.print(" | 在庫：" + foods[result][2]);
					System.out.print(" | 格納場所:" + con.getPlace()[con.getPlace(food_id)]);
					System.out.println(" | 備考:" + foods[result][5] + "\n");
				}
			} else {
				// 一時データ削除一時データ削除
				con.deleteOnetime();
				break;
			}
		} while (true);
		return menunumber[userselect];
	}

}