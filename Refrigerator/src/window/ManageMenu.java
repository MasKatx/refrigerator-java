package window;

import window.general.Input;

/**
 * メニュー管理画面...2
 */
public class ManageMenu extends Default {
	private static String name = "メニュー管理画面";

	public ManageMenu() {
		super(name);
	}

	window.general.Input input = new Input();

	private final String[] menus = { "メニューを追加", "メニューを削除", "メニューを確認", "前の画面に戻る" };
	private final int[] menunumber = { 7, 8, 9, 0 };
	int userselect;

	@Override
	public int process() {
		do {
			// ユーザー選択
			userselect = input.select(menus);
		} while (false);
		return menunumber[userselect];
	}
}