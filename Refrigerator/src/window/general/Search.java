package window.general;

/**
 * 探索系の汎用メソッドをまとめたクラス
 */
public class Search {
	public Search() {
	}

	/**
	 * 文字列を探索する(String用)
	 * 
	 * @param ptn   探索文字列
	 * @param datas 探索対象の文字列が格納された配列
	 * @return 存在していればtrue, していなければfalse
	 */
	public boolean in(String ptn, String[] datas) {
		for (int i = 0; i < datas.length; i++) {
			if (ptn.equals(datas[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 文字列を探索する(int用)
	 * 
	 * @param ptn   探索数値
	 * @param datas 探索対象の数値が格納された配列
	 * @return 存在していればtrue, していなければfalse
	 */
	public boolean in(int ptn, int[] datas) {
		for (int i = 0; i < datas.length; i++) {
			if (ptn == datas[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 文字列を探索し、格納場所を返す(String用)
	 * 
	 * @param ptn   探索文字列
	 * @param datas 探索対象の文字列が格納された配列
	 * @return 探索文字列が格納されている、配列の添え字一覧
	 */
	public int[] where(String ptn, String[] datas) {
		int[] result = new int[datas.length];
		int now = 0;
		for (int i = 0; i < datas.length; i++) {
			if (ptn == datas[i]) {
				result[now] = i;
				now += 1;
			}
		}

		int[] back = new int[now];
		for (int i = 0; i < now; i++) {
			back[i] = result[i];
		}
		return back;
	}

	/**
	 * 数値を探索し、格納場所を返す(int用)
	 * 
	 * @param ptn   探索数値
	 * @param datas 探索対象の数値が格納された配列
	 * @return 探索数値が格納されている、配列の添え字一覧
	 */
	public int[] where(int ptn, int[] datas) {
		int[] result = new int[datas.length];
		int now = 0;
		for (int i = 0; i < datas.length; i++) {
			if (ptn == datas[i]) {
				result[now] = i;
				now += 1;
			}
		}

		int[] back = new int[now];
		for (int i = 0; i < now; i++) {
			back[i] = result[i];
		}
		return back;
	}
}
