package window.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * データベース関係のメソッドをまとめたクラス
 * @author m_katogi
 */
public class Connect {
		//接続に必要な変数を宣言
		private static final String JDBC_URL  = "jdbc:mysql://localhost/refrigerator";
		private static final String USER_ID   = "root";
		private static final String USER_PASS = "cappuccinoSD20";
		private String SQL = "";
		
		public Connect(){}
		
		/**
		 * onetimeへの食品の追加
		 * @param where onetimeと入力(確認用)
		 * @param foodname 食品名
		 * @param stock 個数
		 * @param type 種類
		 * @param limit_type 期限の種類番号
		 * @param note 備考
		 * @param limit_date 期限
		 * @param place_id 場所ID
		 * @return 処理が成功したかどうか
		 */
		public boolean addOnetime(String where, String foodname, int stock, String type, int limit_type, String note, Date limit_date, int place_id) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select food_id from food order by food_id desc;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				rs.next();
				int nextid = rs.getInt("food_id") + 1;
				SQL = "insert into onetime values (?, ?, ?, ?, ?, ?, ?, ?);";
				ps = db.prepareStatement(SQL);
				ps.setInt(1, nextid);
				ps.setString(2, foodname);
				ps.setInt(3, stock);
				ps.setString(4, type);
				ps.setInt(5, limit_type);
				ps.setString(6, note);
				ps.setDate(7, limit_date);
				ps.setInt(8, place_id);
	            ps.executeUpdate();
	            db.close();
				return true;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				e.printStackTrace();
				return false;
			}
		}
		
		/**
		 * 食品の検索
		 * @param foodname 食品名
		 * @return 該当する食品のデータ全て([[id, name, type, limit, note, date],[]...])
		 */
		public String[][] getFood(String foodname){
			ArrayList<ArrayList<String>> now = new ArrayList<ArrayList<String>>();
			String[][] result = new String[1][7];
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select * from food where foodname like '%" + foodname + "%' and food_id > 0;";
				if(foodname.equals("allselect")) {
					SQL = "select * from food where food_id > 0 order by foodname, limit_date;";
				}
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				while(rs.next()){
					ArrayList<String> array = new ArrayList<>();
					array.add(Integer.valueOf(rs.getInt("food_id")).toString());
					array.add(rs.getString("foodname"));
					array.add(Integer.valueOf(rs.getInt("stock")).toString());
					array.add(rs.getString("type"));
					array.add(Integer.valueOf(rs.getInt("limit_type")).toString());
					array.add(rs.getString("note"));
					array.add(new SimpleDateFormat("yyyyMMdd").format(rs.getDate("limit_date")));
					now.add(array);
				}
				result = new String[now.size()][7];
				for(int i=0; i<now.size(); i++) {
					result[i][0] = now.get(i).get(0);
					result[i][1] = now.get(i).get(1);
					result[i][2] = now.get(i).get(2);
					result[i][3] = now.get(i).get(3);
					result[i][4] = now.get(i).get(4);
					result[i][5] = now.get(i).get(5);
					result[i][6] = now.get(i).get(6);
				}
				
	            db.close();
				return result;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				result = new String[0][0];
				return result;
			}
		}

		/**
		 * onetimeに保存されたデータをfoodとrefrigeratorに追加するメソッド
		 * @return 処理が正常終了したかどうか
		 */
		public boolean addFood() {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select * from onetime;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				rs.next();
				int food_id = rs.getInt("food_id");
				String foodname = rs.getString("foodname");
				int stock = rs.getInt("stock");
				String type = rs.getString("type");
				int limit_type = rs.getInt("limit_type");
				String note = rs.getString("note");
				Date limit_date = rs.getDate("limit_date");
				int place_id = rs.getInt("place_id");
				
				SQL = "insert into food values(?, ?, ?, ?, ?, ?, ?);";
				ps = db.prepareStatement(SQL);
				ps.setInt(1, food_id);
				ps.setString(2, foodname);
				ps.setInt(3, stock);
				ps.setString(4, type);
				ps.setInt(5, limit_type);
				ps.setString(6, note);
				ps.setDate(7, limit_date);
				ps.executeUpdate();
				
				SQL = "insert into refrigerator values(?, ?);";
				ps = db.prepareStatement(SQL);
				ps.setInt(1, food_id);
				ps.setInt(2, place_id);
				ps.executeUpdate();
				ps.close();

				deleteOnetime();
				return true;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return false;
			}
		}

		/**
		 * 既存の食品の在庫を追加する処理
		 * @param id 該当食品のID
		 * @return　処理が正常終了したかどうか
		 */
		public boolean addStock(int id) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select * from food where food_id =" + id + ";";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				rs.next();
				int food_id = rs.getInt("food_id");
				String foodname = rs.getString("foodname");
				int stock = rs.getInt("stock");
				String type = rs.getString("type");
				int limit_type = rs.getInt("limit_type");
				String note = rs.getString("note");
				Date limit_date = rs.getDate("limit_date");
				
				SQL = "select stock from onetime;";
				ps = db.prepareStatement(SQL);
				rs = ps.executeQuery(SQL);
				rs.next();
				int result = rs.getInt("stock");
				stock += result;
				
				SQL = "delete from food where food_id =" + food_id + ";";
				ps = db.prepareStatement(SQL);
				ps.executeUpdate();
				
				SQL = "insert into food values(?, ?, ?, ?, ?, ?, ?);";
				ps = db.prepareStatement(SQL);
				ps.setInt(1, food_id);
				ps.setString(2, foodname);
				ps.setInt(3, stock);
				ps.setString(4, type);
				ps.setInt(5, limit_type);
				ps.setString(6, note);
				ps.setDate(7, limit_date);
				ps.executeUpdate();
				ps.close();

				deleteOnetime();
				return true;
			}catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return false;
			}
		}
		
		/**
		 * place_idの取得メソッド
		 * @param id food_id
		 * @return place_id
		 */
		public int getPlace(int id) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select place_id from refrigerator where food_id = " + id + ";";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				rs.next();
				return rs.getInt("place_id");
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
//				e.printStackTrace();
				return 0;
			}
		}

		/**
		 * 既存の食品の在庫を削除する処理
		 * @param id 該当食品のID
		 * @return　処理が正常終了したかどうか
		 */
		@SuppressWarnings("resource")
		public boolean deleteStock(int id, int passstock) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select * from food where food_id =" + id + ";";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				rs.next();
				int food_id = rs.getInt("food_id");
				String foodname = rs.getString("foodname");
				int stock = rs.getInt("stock") - passstock;
				String type = rs.getString("type");
				int limit_type = rs.getInt("limit_type");
				String note = rs.getString("note");
				Date limit_date = rs.getDate("limit_date");

				SQL = "delete from food where food_id =" + food_id + ";";
				ps = db.prepareStatement(SQL);
				ps.executeUpdate();
				
				if(stock > 0) {
					//在庫更新の場合
					SQL = "insert into food values(?, ?, ?, ?, ?, ?, ?);";
					ps = db.prepareStatement(SQL);
					ps.setInt(1, food_id);
					ps.setString(2, foodname);
					ps.setInt(3, stock);
					ps.setString(4, type);
					ps.setInt(5, limit_type);
					ps.setString(6, note);
					ps.setDate(7, limit_date);
				} else {
					//削除の場合
					SQL = "delete from refrigerator where food_id =" + food_id + ";";
					ps = db.prepareStatement(SQL);
				}
				ps.executeUpdate();
				ps.close();
				deleteOnetime();
				return true;
			}catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return false;
			}
		}
		
		/**
		 * onetimeに保管された食品名を取得するメソッド
		 * @return 食品名
		 */
		public String getOnetimeFoodname() {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select foodname from onetime;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				rs.next();
				String result = rs.getString("foodname");
				ps.close();
				return result;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				e.printStackTrace();
				return "";
			}
		}

		/**
		 * onetimeに格納された内容を削除するメソッド
		 * @return 処理が正常終了したかどうか
		 */
		public boolean deleteOnetime() {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "delete from onetime;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ps.executeUpdate();
				ps.close();
				return true;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return false;
			}
		}
		
		/**
		 * 格納場所の一覧を検索
		 * @return 格納場所の一覧
		 */
		public String[] getPlace() {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select placename from placename;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				String[] data = new String[100];
				int count = 0;
				while(rs.next()) {
					data[count] = rs.getString("placename");
					count += 1;
				}
				ps.close();
				String[] result = new String[count];
				for(int i=0; i<count; i++) {
					result[i] = data[i];
				}
				return result;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				e.printStackTrace();
				return new String[0];
			}
		}

		/**
		 * メニューを新規追加するメソッド
		 * @param menu_name メニュー名
		 * @param content レシピ内容
		 * @param food 使用仕様食品とその個数一覧
		 * @return 処理が正常終了したかどうか
		 */
		public boolean addMenu(String menu_name, String content, String[][] food) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select menu_id from menu order by menu_id desc;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				rs.next();
				int menu_id = rs.getInt("menu_id") + 1;
				
				SQL = "insert into menu values(?, ?, ?);";
				ps = db.prepareStatement(SQL);
				ps.setInt(1, menu_id);
				ps.setString(2, menu_name);
				ps.setString(3, content);
				ps.executeUpdate();
				
				SQL = "insert into menufood values(?, ?, ?);";
				ps = db.prepareStatement(SQL);
				for(int i=0; i<food.length; i++) {
					ps.setString(1, food[i][0]);
					ps.setInt(2, Integer.parseInt(food[i][1]));
					ps.setInt(3, menu_id);
					ps.executeUpdate();
				}
				ps.close();
				return true;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return false;
			}
		}

		/**
		 * 該当メニューに存在する食品の、冷蔵庫内の在庫を求めるメソッド
		 * @param food　使用食品名一覧
		 * @return 与えられた食品名の順番に格納された在庫数
		 */
		public int[] judgeMenu(String[] food){
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				int[] result = new int[food.length];
				int count = 0;
				for(int i=0; i<food.length; i++) {
					count = 0;
					SQL = "select stock from food where foodname = '" + food[i] + "';";
					PreparedStatement ps = db.prepareStatement(SQL);
					ResultSet rs = ps.executeQuery(SQL);
					while(rs.next()) {
						count += rs.getInt("stock");
					}
					result[i] = count;
				}
				return result;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return new int[0];
			}
		}

		/**
		 * menunameから、類似するメニューデータを一覧で返すメソッド
		 * @param menuname メニュー名
		 * @return [[メニューID, メニュー名, レシピ内容], []...]
		 */
		public String[][] checkMenu(String menuname){
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				String[][] result = new String[100][3];
				String[][] result2;
				SQL = "select * from menu where title like '%" + menuname + "%' and menu_id > 0 order by title;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				int count = 0;
				while(rs.next()) {
					result[count][0] = Integer.toString(rs.getInt("menu_id"));
					result[count][1] = rs.getString("title");
					result[count][2] = rs.getString("content");
					count ++;
				}
				result2 = new String[count][3];
				for(int i=0; i<count; i++) {
					result2[i][0] = result[i][0];
					result2[i][1] = result[i][1];
					result2[i][2] = result[i][2];
				}
				return result2;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return new String[0][0];
			}
		}

		/**
		 * menu_idから、一致する使用食品の名称と個数の一覧を取得するメソッド
		 * @param menu_id
		 * @return [[食品名, 必要個数], []...]
		 */
		public String[][] getUsedFoods(int menu_id) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				String[][] result = new String[100][2];
				String[][] result2;
				SQL = "select * from menufood where menufood_id = " + menu_id + ";";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				int count = 0;
				while(rs.next()) {
					result[count][0] = rs.getString("menufood_name");
					result[count][1] = Integer.toString(rs.getInt("number"));
					count ++;
				}
				
				result2 = new String[count][2];
				for(int i=0; i<count; i++) {
					result2[i][0] = result[i][0];
					result2[i][1] = result[i][1];
				}
				return result2;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return new String[0][0];
			}
		}

		/**
		 * menu_idで指定したメニューに関するデータを削除するメソッド
		 * @param menu_id メニューID
		 * @return 処理が正常終了したかどうか
		 */
		public boolean deleteMenu(int menu_id) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "delete from menu where menu_id = " + menu_id + ";";
				PreparedStatement ps = db.prepareStatement(SQL);
				ps.executeUpdate(SQL);
				SQL = "delete from menufood where menufood_id = " + menu_id + ";";
				ps = db.prepareStatement(SQL);
				ps.executeUpdate(SQL);
				return true;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return false;
			}
		}

		/**
		 * メモを新規追加するメソッド
		 * @param memo メモデータ
		 * @return 処理が正常終了したかどうか
		 */
		public boolean addMemo(String[] memo) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "select memo_id from memo order by memo_id desc;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				rs.next();
				int memo_id = rs.getInt("memo_id") + 1;
				
				SQL = "insert into memo values(?, ?, ?);";
				ps = db.prepareStatement(SQL);
				ps.setInt(1, memo_id);
				ps.setString(2, memo[0]);
				ps.setString(3, memo[1]);
				ps.executeUpdate();
				ps.close();
				return true;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return false;
			}
		}

		/**
		 * メモの一覧を取得するメソッド
		 * @return メモデータの一覧...[メモタイトル, メモ内容, メモID]
		 */
		public String[][] getMemo(){
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				String[][] set = new String[100][3];
				SQL = "select * from memo where memo_id > 0 order by title;";
				PreparedStatement ps = db.prepareStatement(SQL);
				ResultSet rs = ps.executeQuery(SQL);
				int count = 0;
				while(rs.next()) {
					set[count][0] = rs.getString("title");
					set[count][1] = rs.getString("content");
					set[count][2] = Integer.toString(rs.getInt("memo_id"));
					count++;
				}
				String[][] result = new String[count][3];
				for(int i=0; i<count; i++) {
					result[i][0] = set[i][0];
					result[i][1] = set[i][1];
					result[i][2] = set[i][2];
				}
				ps.close();
				return result;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return new String[0][0];
			}
		}

		/**
		 * メモを削除するメソッド
		 * @param memo_id メモID
		 * @return 処理が正常終了したかどうか
		 */
		public boolean deleteMemo(int memo_id) {
			try {
				Connection db = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
//				System.out.println("DBへの接続に成功しました。");
				SQL = "delete from memo where memo_id = " + memo_id + ";";
				PreparedStatement ps = db.prepareStatement(SQL);
				ps.executeUpdate(SQL);
				ps.close();
				return true;
			} catch(SQLException e) {
				System.out.println("DBへの接続に失敗しました。強制終了します。");
				return false;
			}
		}
}
