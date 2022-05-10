package window;

import window.database.Connect;
import window.general.Input;

/**
 * メモ確認画面...11
 */
public class CheckMemo extends Default {
	private static String name = "メモ確認画面";

	public CheckMemo() {
		super(name);
	}

	window.general.Input input = new Input();

	private final String[] menus = { "メモの一覧を見る", "前の画面に戻る", "メニューに戻る" };
	private final int[] menunumber = { 99, 3, 0 };
	private int userselect;

	@Override
	public int process() {
		// DB接続
		window.database.Connect con = new Connect();
		String[][] memo = con.getMemo();
		String[] title = new String[memo.length];
		String[] cutcont = new String[memo.length];
		String[] pass = new String[memo.length + 1];
		String[] deleteor = { "このメモを削除する", "メモの一覧に戻る" };
		int memoselect = 0;
		pass[memo.length] = "前の画面に戻る";
		for (int i = 0; i < memo.length; i++) {
			title[i] = memo[i][0];
			pass[i] = memo[i][0];
			if (memo[i][1].length() <= 20) {
				cutcont[i] = memo[i][1];
			} else {
				cutcont[i] = (memo[i][1]).substring(0, 20) + "...";
			}
		}
		do {
			// ユーザー選択
			userselect = input.select(menus);
			if (userselect == 0) {
				for (int i = 0; i < memo.length; i++) {
					System.out.print(i + ":タイトル:" + title[i]);
					System.out.println(" | 内容:" + cutcont[i]);
				}
				memoselect = input.select(pass);
				if (memoselect == memo.length) {
					// 前の画面に戻る
					continue;
				} else {
					// 該当メモを選択
					System.out.println("タイトル:" + title[memoselect]);
					System.out.println("内容:" + memo[memoselect][1] + "\n");
					memoselect = input.select(deleteor);
					if (memoselect == 0) {
						System.out.println("本当に削除してよろしいですか？");
						memoselect = input.select(deleteor);
						if (memoselect == 1) {
							continue;
						}
						// 削除処理
						if (con.deleteMemo(Integer.parseInt(memo[memoselect][2]))) {
							System.out.println("このメモを削除しました。");
						} else {
							System.out.println("エラーが起こりました。最初からやり直してください。");
						}
					}
				}
			} else {
				break;
			}
		} while (true);
		return menunumber[userselect];
	}

}