package window.general;

/**
 * 検証用の汎用メソッドをまとめたクラス
 */
public class Check {
	public Check() {
	}

	/**
	 * 引数が数字かどうかの判定メソッド
	 * 
	 * @param word 判定する文字列
	 * @return 数値変換可能でtrue
	 */
	public boolean CheckStringToInteger(String word) {
		try {
			Integer.parseInt(word);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}