package window;
import window.database.Connect;
import window.general.Input;

/**
 * システムメニュー...0
 */
public class SystemMenu extends Default{
	private static String name = "システムメニュー";
	public SystemMenu(){
		super(name);
	}
	
	window.general.Input input = new Input();

	private final String[] menus = {"食品管理", "メニュー管理", "メモ管理", "キャッシュクリア(DBとの接続が上手くいかない場合にお試しください)", "終了"};
	private final int[] menunumber = {1, 2, 3, 90, 99};
	int userselect;
	
	@Override
	public int process() {
		while(true) {
			userselect = input.select(menus);
			if(userselect == 3) {
				window.database.Connect con = new Connect();
				con.deleteOnetime();
				System.out.println("一時データ等の削除を行いました。");
			} else {
				break;
			}
		}
		return menunumber[userselect];
	}
	
}