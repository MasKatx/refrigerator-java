package window;

import window.database.Connect;
import window.general.Input;

/**
 * 食品削除確認画面...13
 */
public class CheckDeleteFood extends Default {
	private static String name = "食品選択確認画面";

	public CheckDeleteFood() {
		super(name);
	}

	window.general.Input input = new Input();

	private final String[] menus = { "類似した食品を確認", "前の画面に戻る", "メニューに戻る" };
	private final int[] menunumber = { 0, 5, 0 };
	private int stock = 0;
	private int userselect = 0;
	private int food_id = 0;
	private String[] pass = { "在庫数" };

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
					System.out.println("処理を中断しました");
				} else {
					// 在庫数の入力、削除
					food_id = Integer.parseInt(foods[result][0]);
					while (true) {
						stock = inter.Type.typeInt(pass)[0];
						if (stock < 0) {
							System.out.println("正の整数で入力してください。");
						} else {
							break;
						}
					}
					// 在庫削減処理
					if (con.deleteStock(food_id, stock)) {
						System.out.println("食品から在庫を削除しました。システムメニューに戻ります。");
					} else {
						con.deleteOnetime();
						System.out.println("エラーが起きました。恐れ入りますが、最初からやり直してください。");
					}
					break;
				}
			} else {
				// 一時データ削除
				con.deleteOnetime();
				break;
			}
		} while (true);
		return menunumber[userselect];
	}

}