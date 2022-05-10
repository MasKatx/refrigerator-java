import window.AddFood;
import window.AddMemo;
import window.AddMenu;
import window.CheckAddFood;
import window.CheckDeleteFood;
import window.CheckFood;
import window.CheckMemo;
import window.CheckMenu;
import window.CheckSelectFood;
import window.DeleteFood;
import window.DeleteMenu;
import window.ManageFood;
import window.ManageMemo;
import window.ManageMenu;
import window.SystemMenu;

public class Main {
	public static void main(String[] args) {
		int nextwindow = 0;
		System.out.println("冷蔵庫管理システムを起動します。");
		// 以下終了命令まで繰り返し
		while (nextwindow != 99) {
			if (nextwindow == 0) {
				window.SystemMenu win0 = new SystemMenu();
				nextwindow = win0.process();
			} else if (nextwindow == 1) {
				window.ManageFood win1 = new ManageFood();
				nextwindow = win1.process();
			} else if (nextwindow == 2) {
				window.ManageMenu win2 = new ManageMenu();
				nextwindow = win2.process();
			} else if (nextwindow == 3) {
				window.ManageMemo win3 = new ManageMemo();
				nextwindow = win3.process();
			} else if (nextwindow == 4) {
				window.AddFood win4 = new AddFood();
				nextwindow = win4.process();
			} else if (nextwindow == 5) {
				window.DeleteFood win5 = new DeleteFood();
				nextwindow = win5.process();
			} else if (nextwindow == 6) {
				window.CheckFood win6 = new CheckFood();
				nextwindow = win6.process();
			} else if (nextwindow == 7) {
				window.AddMenu win7 = new AddMenu();
				nextwindow = win7.process();
			} else if (nextwindow == 8) {
				window.DeleteMenu win8 = new DeleteMenu();
				nextwindow = win8.process();
			} else if (nextwindow == 9) {
				window.CheckMenu win9 = new CheckMenu();
				nextwindow = win9.process();
			} else if (nextwindow == 10) {
				window.AddMemo win10 = new AddMemo();
				nextwindow = win10.process();
			} else if (nextwindow == 11) {
				window.CheckMemo win11 = new CheckMemo();
				nextwindow = win11.process();
			} else if (nextwindow == 12) {
				window.CheckAddFood win12 = new CheckAddFood();
				nextwindow = win12.process();
			} else if (nextwindow == 13) {
				window.CheckDeleteFood win13 = new CheckDeleteFood();
				nextwindow = win13.process();
			} else if (nextwindow == 14) {
				window.CheckSelectFood win14 = new CheckSelectFood();
				nextwindow = win14.process();
			}
		}
		System.out.println("終了します。");

	}
}
