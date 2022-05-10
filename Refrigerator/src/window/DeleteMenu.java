package window;

import window.database.Connect;
import window.general.Input;

/**
 * メニュー削除画面...2
 */
public class DeleteMenu extends Default implements inter.Type {
	private static String name = "メニュー削除画面";

	public DeleteMenu() {
		super(name);
	}

	window.general.Input input = new Input();

	private String[] menus = { "メニュー名の入力", "決定", "前の画面に戻る", "メニューに戻る" };
	private final int[] menunumber = { 99, 0, 2, 0 };
	private final String[] passname = { "メニュー名" };
	private int userselect;
	private boolean flg = false;
	private String menuname = "";

	@Override
	public int process() {
		// DB接続
		window.database.Connect con = new Connect();
		do {
			// ユーザー選択
			userselect = input.select(menus);
			if (userselect == 0) {
				// 入力受付
				menuname = inter.Type.typeString(passname)[0];
				flg = true;
				menus[0] = "メニュー名の再入力";
			} else if (userselect == 1 && flg == false) {
				printError();
			} else if (userselect == 1) {
				// メニュー取得
				String[][] foodmenus = con.checkMenu(menuname);
				String[] menunames = new String[foodmenus.length + 1];
				for (int i = 0; i < foodmenus.length; i++) {
					menunames[i] = foodmenus[i][1];
				}
				menunames[foodmenus.length] = "メニュー削除画面に戻る";
				if (foodmenus.length == 0) {
					System.out.println("該当するメニューが存在しませんでした。");
				}
				int menuselect = input.select(menunames);
				if (menuselect == foodmenus.length) {
					continue;
				}

				// 入力受付
				String[][] usedfood = con.getUsedFoods(Integer.parseInt(foodmenus[menuselect][0]));
				String[] usedfoodname = new String[usedfood.length];
				int[] usedfoodnumber = new int[usedfood.length];
				for (int i = 0; i < usedfood.length; i++) {
					usedfoodname[i] = usedfood[i][0];
					usedfoodnumber[i] = Integer.parseInt(usedfood[i][1]);
				}

				// 情報表示
				System.out.println("メニュー名:" + foodmenus[menuselect][1]);
				System.out.println("レシピ：" + foodmenus[menuselect][2]);
				for (int i = 0; i < usedfood.length; i++) {
					System.out.print("食品名：" + usedfoodname[i]);
					System.out.println(" | 必要個数：" + usedfoodnumber[i]);
				}
				System.out.println("削除しますか？");
				String[] question = { "削除する", "メニュー名の入力に戻る" };
				int answer = input.select(question);
				if (answer == 0) {
					// 削除処理
					if (con.deleteMenu(Integer.parseInt(foodmenus[menuselect][0]))) {
						System.out.println("削除しました");
					} else {
						System.out.println("エラーが発生しました。最初からやり直してください。");
					}
					break;
				} else {
					continue;
				}
			} else {
				break;
			}
		} while (true);
		return menunumber[userselect];
	}

	@Override
	public void printError() {
		System.out.println("メニュー名を入力してください");
		userselect = 0;
	}
}