package window;

import java.sql.Date;

import window.database.Connect;
import window.general.Input;

/**
 * 食品確認画面...6
 */
public class CheckFood extends Default implements inter.Type {
	private static String name = "食品確認画面";

	public CheckFood() {
		super(name);
	}

	window.general.Input input = new Input();

	private String[] menus = { "食品名の入力", "決定", "全て表示する", "前の画面に戻る", "メニューに戻る" };
	private final int[] menunumber = { 99, 14, 14, 1, 0 };
	private final String[] passname = { "食品名" };
	private int userselect;
	private boolean flg = false;
	private String foodname = "";

	@Override
	public int process() {
		// DB接続
		window.database.Connect con = new Connect();
		@SuppressWarnings("deprecation")
		Date date = new Date(2020 - 1900, 0, 1);
		do {
			// ユーザー選択
			userselect = input.select(menus);
			if (userselect == 0) {
				// 入力受付
				foodname = inter.Type.typeString(passname)[0];
				flg = true;
				menus[0] = "食品名の再入力";
			} else if (userselect == 2) {
				// 一時データに情報を追加
				con.addOnetime("onetime", "allselect", 0, "", 0, "", date, 0);
				break;
			} else if (userselect == 1 && flg == false) {
				printError();
			} else if (userselect == 1) {
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