package model.service;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * サービスクラス共通のDB接続処理を提供する基底クラス。
 */
public abstract class BaseService {

	/**
	 * DBコネクションを取得する。
	 * 
	 * @return コネクションオブジェクト
	 * @throws Exception JDBCエラー時
	 */
	protected Connection getConnection() throws Exception {
		String url = "jdbc:mysql://localhost:3306/f2" + "?useSSL=false" + "&characterEncoding=utf8"
				+ "&serverTimezone=Asia/Tokyo";

		String user = "root";
		String password = "password"; // ← 実際のMySQLパスに合わせて

		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(url, user, password);
	}
}