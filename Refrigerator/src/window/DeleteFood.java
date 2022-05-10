package window;

import java.sql.Date;

import window.database.Connect;
import window.general.Input;

/**
 * 食品削除画面...5
 */
public class DeleteFood extends Default implements inter.Type {
	private static String name = "食品削除画面";

	public DeleteFood() {
		super(name);
	}

	window.general.Input input = new Input();

	private String[] menus = { "食品名の入力", "決定", "前の画面に戻る", "メニューに戻る" };
	private final int[] menunumber = { 99, 13, 1, 0 };
	private final String[] passname = { "食品名" };
	private int userselect;
	private boolean flg = false;
	private String foodname = "";

	@Override
	public int process() {
		do {
			// ユーザー選択
			userselect = input.select(menus);
			if (userselect == 0) {
				foodname = inter.Type.typeString(passname)[0];
				flg = true;
				menus[0] = "食品名の再入力";
			} else if (userselect == 1 && flg == false) {
				printError();
			} else if (userselect == 1) {
				// DB接続
				window.database.Connect con = new Connect();
				// 一時データ追加
				@SuppressWarnings("deprecation")
				Date date = new Date(2020 - 1900, 0, 1);
				con.addOnetime("onetime", foodname, 0, "", 0, "", date, 0);
				break;
			} else {
				break;
			}
		} while (true);
		return menunumber[userselect];
	}

	@Override
	public void printError() {
		System.out.println("食品名を入力してください");
		userselect = 0;
	}
}