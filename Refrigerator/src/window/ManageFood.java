package window;

import window.general.Input;

/**
 * 食品管理画面...1
 */
public class ManageFood extends Default {
	private static String name = "食品管理画面";

	public ManageFood() {
		super(name);
	}

	window.general.Input input = new Input();

	private final String[] menus = { "食品を追加", "食品を削除", "食品を確認", "前の画面に戻る" };
	private final int[] menunumber = { 4, 5, 6, 0 };
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