package inter;

import window.general.Check;
import window.general.Input;

public interface Type {
	window.general.Input input = new Input();
	window.general.Check check = new Check();

	/**
	 * 一般的な入力を受け付けるメソッド
	 * 
	 * @param datas 入力させる内容の一覧
	 * @return 入力値一覧
	 */
	static String[] typeString(String[] datas) {
		return input.data(datas);
	}

	/**
	 * 数値での入力を受け付けるメソッド
	 * 
	 * @param datas 入力させるデータの一覧
	 * @return 入力値一覧
	 */
	static int[] typeInt(String[] datas) {
		String[] back = input.data(datas);
		int[] result = new int[datas.length];
		for (int i = 0; i < datas.length; i++) {
			if (check.CheckStringToInteger(back[i]) == false) {
				System.out.println("整数での入力が必要です。");
				while (true) {
					try {
						result[i] = Integer.parseInt(input.name(datas[i]));
						break;
					} catch (NumberFormatException e) {
						System.out.println("整数を入力してください");
					}
				}
			} else {
				result[i] = Integer.parseInt(back[i]);
			}
		}
		return result;
	}

	/**
	 * 選択させるメソッド
	 * 
	 * @param datas 選択肢一覧
	 * @return 選択解答
	 */
	static int select(String[] datas) {
		return input.select(datas);
	}

	/**
	 * 日付を入力させて返すメソッド
	 * 
	 * @param data 入力させる日付
	 * @return [年, 月, 日]
	 */
	static int[] date(String data) {
		int[] result = new int[3];
		String[] name = { "年", "月", "日" };
//		input.name("以下に期限");
		for (int i = 0; i < 3; i++) {
			while (true) {
				try {
					result[i] = Integer.parseInt(input.name(data + "..." + name[i]));
					if (i == 0) {
						if (result[i] >= 2021 && result[i] < 2050) {
							break;
						}
					} else if (i == 1) {
						if (result[i] > 0 && result[i] <= 12) {
							break;
						}
					} else {
						if (result[1] == 1 || result[1] == 3 || result[1] == 5 || result[1] == 7 || result[1] == 8
								|| result[1] == 10 || result[1] == 12) {
							if (result[i] > 0 && result[i] <= 31) {
								break;
							}
						} else if (result[1] == 2) {
							if (result[0] % 4 == 0 && result[0] % 100 != 0 || result[0] % 400 == 0) {
								if (result[i] > 0 && result[i] <= 29) {
									break;
								}
							} else {
								if (result[i] > 0 && result[i] <= 28) {
									break;
								}
							}
						} else {
							if (result[i] > 0 && result[i] <= 30) {
								break;
							}
						}
					}
					System.out.println("正しい値を入力してください");
				} catch (NumberFormatException e) {
					System.out.println("整数で入力してください");
				}
			}
		}
		return result;
	}

	/**
	 * Typeを継承する場合、エラーメッセージ表示メソッドのオーバーライドを義務化
	 */
	void printError();
}