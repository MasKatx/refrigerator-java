package window.general;

import java.util.Scanner;

/**
 * 入力用の汎用メソッドをまとめたクラス
 */
public class Input {
	Scanner jin = new Scanner(System.in);

	public Input() {
	}

	/**
	 * 一般的な入力
	 * 
	 * @param name 入力してもらう文字列の名前
	 * @return 入力された文字列
	 */
	public String name(String name) {
		System.out.println(name + "を入力してください");
		String result = jin.next();
		return result;
	}

	/**
	 * 複数入力
	 * 
	 * @param data 入力してもらう文字列の一覧
	 * @return 入力された文字列の一覧
	 */
	public String[] data(String[] data) {
		String[] result = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i] + "を入力してください");
			result[i] = jin.next();
		}
		return result;
	}

	/**
	 * 選択肢を与えてそこから選ぶ入力
	 * 
	 * @param data 選択肢の一覧
	 * @return 入力された整数値(選択肢のインデックス値)
	 */
	public int select(String[] data) {
		System.out.println("該当する整数を選択してください");
		for (int i = 0; i < data.length; i++) {
			System.out.println(i + ":" + data[i]);
		}
		int result = 0;
		while (true) {
			System.out.print("入力：");
			try {
				result = jin.nextInt();
				data[result] = data[result];
				break;
			} catch (java.util.InputMismatchException e) {
				System.out.println("整数を入力してください");
				jin.next();
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				System.out.println("範囲内の値を入力してください");
			}
		}
		return result;

	}

}