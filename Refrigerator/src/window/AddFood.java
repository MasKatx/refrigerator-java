package window;

import java.sql.Date;

import window.database.Connect;
import window.general.Input;

/**
 * 食品追加画面...4
 */
public class AddFood extends Default implements inter.Type {
	private static String name = "食品追加画面";

	public AddFood() {
		super(name);
	}

	window.general.Input input = new Input();

	private String[] menus = { "食品データの入力", "決定", "前の画面に戻る", "メニューに戻る" };
	private final String[] datas = { "食品名(20字以内)", "種類(20字以内)", "備考(100字以内)" };
	private final String[] number = { "個数" };
	private final String[] limit = { "賞味期限", "消費期限" };
	private String limitdate;
	private final int[] menunumber = { 99, 12, 1, 0 };
	private int userselect;
	private boolean flg = false;

	@Override
	public int process() {
		// DB接続
		window.database.Connect con = new Connect();
		String[] data1 = new String[3];
		int data2 = 0;
		int data3 = 0;
		int[] data4 = new int[3];
		int data5 = 0;
		String[] place = con.getPlace();

		do {
			// ユーザーの選択
			userselect = input.select(menus);
			if (userselect == 0) {
				/**
				 * 文字系食品データ
				 */
				while (true) {
					data1 = inter.Type.typeString(datas); // data1入力
					if (data1[0].length() <= 20 && data1[1].length() <= 20 && data1[2].length() <= 100) {
						break;
					}
					System.out.println("もう一度指定文字数以内で入力しなおしてください。");
				}
				/**
				 * 個数
				 */
				data2 = inter.Type.typeInt(number)[0]; // data2入力
				/**
				 * 賞味期限or消費期限
				 */
				data3 = inter.Type.select(limit); // data3入力
				limitdate = limit[data3];
				/**
				 * 期限の日付
				 */
				data4 = inter.Type.date(limitdate); // data4入力
				/**
				 * 格納場所
				 */
				data5 = inter.Type.select(place); // data5入力
				flg = true;
				menus[0] = "食品データの再入力(最初から入力)";
			} else if (userselect == 1 && flg == false) {
				printError();
			} else if (userselect == 1) {
				// 追加処理
				@SuppressWarnings("deprecation")
				Date date = new Date(data4[0] - 1900, data4[1] - 1, data4[2]);
				con.addOnetime("onetime", data1[0], data2, data1[1], data3, data1[2], date, data5);
				break;
			} else {
				break;
			}
		} while (true);
		return menunumber[userselect];
	}

	@Override
	public void printError() {
		System.out.println("食品データを入力してください");
		userselect = 0;
	}
}