package window;

import window.database.Connect;
import window.general.Input;
import window.general.Search;

/**
 * メニュー確認画面...9
 */
public class CheckMenu extends Default implements inter.Type {
	private static String name = "メニュー確認画面";

	public CheckMenu() {
		super(name);
	}

	window.general.Input input = new Input();
	window.general.Search search = new Search();

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
				// 一覧表示
				String[][] foodmenus = con.checkMenu(menuname);
				String[] menunames = new String[foodmenus.length + 1];
				for (int i = 0; i < foodmenus.length; i++) {
					menunames[i] = foodmenus[i][1];
				}
				menunames[foodmenus.length] = "メニュー確認画面に戻る";
				if (foodmenus.length == 0) {
					System.out.println("該当するメニューが存在しませんでした。");
				}
				int menuselect = input.select(menunames);
				if (menuselect == foodmenus.length) {
					// メニュー確認画面に戻る
					continue;
				}
				// 使用食品一覧
				String[][] usedfood = con.getUsedFoods(Integer.parseInt(foodmenus[menuselect][0]));
				String[] usedfoodname = new String[usedfood.length];
				int[] usedfoodnumber = new int[usedfood.length];
				for (int i = 0; i < usedfood.length; i++) {
					usedfoodname[i] = usedfood[i][0];
					usedfoodnumber[i] = Integer.parseInt(usedfood[i][1]);
				}

				// 在庫だけで作れるかどうか
				boolean flg = true;
				String[][] foods = con.getFood("allselect");
				String[] foodnames = new String[foods.length];
				for(int i=0; i<foods.length; i++) {
					foodnames[i] = foods[i][1];
				}
				
				//該当食品が存在しない場合
				for(int i=0; i<usedfoodname.length; i++) {
					flg = search.in(usedfoodname[i], foodnames);
				}
				
				//全て該当食品がある場合、在庫を確認
				int[] usedfoodstock = con.judgeMenu(usedfoodname);
				if(flg) {
					for (int i = 0; i < usedfood.length; i++) {
						if (usedfoodnumber[i] > usedfoodstock[i]) {
							System.out.println(usedfoodnumber[i] + " " + usedfoodstock[i]);
							flg = false;
							break;
						}
					}
				}

				// 以下結果表示
				if (flg) {
					System.out.println("冷蔵庫内の食品だけでこのメニューは作ることができます。");
				} else {
					System.out.println("注：冷蔵庫内の食品ではこのメニューは作ることができません。");
				}

				System.out.println("レシピ：" + foodmenus[menuselect][2]);
				for (int i = 0; i < usedfood.length; i++) {
					System.out.print("食品名：" + usedfoodname[i]);
					System.out.print(" | 必要個数：" + usedfoodnumber[i]);
					System.out.println(" | 在庫数：" + usedfoodstock[i]);
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
		System.out.println("メニュー名を入力してください");
		userselect = 0;
	}
}