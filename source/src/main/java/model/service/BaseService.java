package model.service;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * サービスクラス共通のDB接続処理を提供する基底クラス。
 */
public abstract class BaseService {

    /**
     * DBコネクションを取得する。
     * @return コネクションオブジェクト
     * @throws Exception JDBCエラー時
     */
    protected Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/f2"
                   + "?useUnicode=true"
                   + "&characterEncoding=UTF-8"
                   + "&serverTimezone=Asia/Tokyo"
                   + "&useSSL=false";

        String user = "root";
        String password = "password"; // 環境に合わせて調整

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}