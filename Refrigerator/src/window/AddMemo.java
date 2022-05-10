package window;

import window.database.Connect;
import window.general.Input;

/**
 * メモ追加画面...10
 */
public class AddMemo extends Default implements inter.Type {
	private static String name = "メモ追加画面";

	public AddMemo() {
		super(name);
	}

	window.general.Input input = new Input();

	private String[] menus = { "メモデータの入力", "メモを追加", "前の画面に戻る", "メニューに戻る" };
	private final String[] datas = { "メモタイトル(20字以内)", "メモ内容(200字以内)" };
	private final int[] menunumber = { 99, 0, 3, 0 };
	private int userselect;
	private boolean flg = false;

	@Override
	public int process() {
		// DB接続
		window.database.Connect con = new Connect();
		String[] memo = new String[2];
		do {
			// ユーザーの選択
			userselect = input.select(menus);
			if (userselect == 0) {
				while (true) {
					memo = inter.Type.typeString(datas);
					if (memo[0].length() <= 20 && memo[1].length() <= 200) {
						break;
					}
					System.out.println("もう一度指定文字数以内で入力しなおしてください。");
				}
				flg = true;
				menus[0] = "メモデータの再入力(最初から入力)";
			} else if (userselect == 1 && flg == false) {
				printError();
			} else if (userselect == 1) {
				// 追加処理
				if (con.addMemo(memo)) {
					System.out.println("メモを新規追加しました。");
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
		System.out.println("メモデータを入力してください");
		userselect = 0;
	}
}