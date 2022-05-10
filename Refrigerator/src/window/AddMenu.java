package window;

import window.database.Connect;
import window.general.Input;

/**
 * メニュー追加画面...7
 */
public class AddMenu extends Default implements inter.Type {
	private static String name = "メニュー追加画面";

	public AddMenu() {
		super(name);
	}

	window.general.Input input = new Input();

	private String[] menus = { "メニューデータの入力", "メニューを追加", "前の画面に戻る", "メニューに戻る" };
	private final String[] datas = { "メニュー名(20字以内)" };
	private final String[] number1 = { "食品名(20字以内)" };
	private final String[] number2 = { "個数" };
	private final String[] content = { "レシピ内容(合計1000字以内)" };
	private final int[] menunumber = { 99, 0, 2, 0 };
	private int userselect;
	private boolean flg = false;

	@Override
	public int process() {
		// DB接続
		window.database.Connect con = new Connect();
		String menu_name = "";
		String[][] food = new String[100][2];
		String[][] food_result = {};
		String[] pass = { "更に追加", "追加を終了" };
		String[] text = new String[2];
		int count = 0;
		do {
			// ユーザーの選択
			userselect = input.select(menus);
			if (userselect == 0) {
				while (true) {
					menu_name = inter.Type.typeString(datas)[0];
					if (menu_name.length() <= 20) {
						break;
					}
					System.out.println("もう一度指定文字数以内で入力しなおしてください。");
				}

				// ユーザーが希望する限り食品データを入力させる
				System.out.println(
						"以下、使用する食品欄に調味料は追加しないでください。冷蔵庫内の食品で作れるかの判断が正常にできなくなります。\nどうしても追加したい場合は、調味料を食品として追加し、在庫数を十分大きくしてください。");
				while (true) {
					while (true) {
						food[count][0] = inter.Type.typeString(number1)[0];
						if (food[count][0].length() <= 20) {
							break;
						}
						System.out.println("もう一度指定文字数以内で入力しなおしてください。");
					}
					food[count][1] = Integer.toString(inter.Type.typeInt(number2)[0]);
					count += 1;
					if (input.select(pass) == 1) {
						break;
					}
				}
				food_result = new String[count][2];
				for (int i = 0; i < count; i++) {
					food_result[i] = food[i];
				}

				// ユーザーが希望する限りレシピ工程を入力させる
				while (true) {
					text[1] = inter.Type.typeString(content)[0];
					if (text[0] != null && text[0].length() + text[1].length() > 1000) {
						System.out.println("文字数が制限を超えました。強制的に現在の入力を消去してデータの入力を終了します。");
						break;
					}
					if (text[0] == null) {
						text[0] = text[1];
					} else {
						text[0] = text[0] + "  |  " + text[1];
					}
					if (input.select(pass) == 1) {
						break;
					}
				}

				flg = true;
				menus[0] = "メニューデータの再入力(最初から入力)";
			} else if (userselect == 1 && flg == false) {
				printError();
			} else if (userselect == 1) {
				// 追加処理
				if (con.addMenu(menu_name, text[0], food_result)) {
					System.out.println("新規メニューとして追加しました。");
				} else {
					System.out.println("エラーが起こりました。最初からやり直してください。");
				}
				break;
			} else {
				break;
			}
		} while (true);
		return menunumber[userselect];
	}

	@Override
	public void printError() {
		System.out.println("メニューデータを入力してください");
		userselect = 0;
	}
}