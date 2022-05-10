package window;

import window.general.Input;

/**
 * メモ管理画面...3
 */
public class ManageMemo extends Default {
	private static String name = "メモ管理画面";

	public ManageMemo() {
		super(name);
	}

	window.general.Input input = new Input();

	private final String[] menus = { "メモを追加", "メモを確認/削除", "前の画面に戻る" };
	private final int[] menunumber = { 10, 11, 0 };
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