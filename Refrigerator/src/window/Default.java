package window;

/**
 * 各windowの雛形となる抽象クラス
 */
abstract class Default implements inter.Process {

	/**
	 * 起動後の画面名表示
	 * 
	 * @param name 画面名(各ウィンドウで定義)
	 */
	Default(String name) {
		System.out.println("------------------\n---" + name + "---");
	}

	int userselect;

	@Override
	public abstract int process();
}