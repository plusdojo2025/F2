package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.dto.UserDto;

public class UserDao {

	/* db接続 */
	private static final String URL = "jdbc:mysql://localhost:3306/f2";
	private static final String USER = "root";
	private static final String PASS = "password";

	private Connection conn; // 追加: インスタンス変数

	// 追加: Connectionを受け取るコンストラクタ
	public UserDao(Connection conn) {
		this.conn = conn;
	}

	// 既存の引数なしコンストラクタも残す場合
	public UserDao() {
	}

	/* ユーザー登録 */
	public boolean registerUser(UserDto user) throws SQLException {
		String sql = "INSERT INTO users(username,email,password_hash) VALUES (?,?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPasswordHash());

			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;

		}
	}

	/* ログイン認証（メールとパスワードハッシュが一致するかチェック） */
	public UserDto findByEmailAndPassword(String email, String passwordHash) {
		String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			stmt.setString(2, passwordHash);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {

				return new UserDto(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"),
						rs.getString("password_hash"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* メールアドレスでユーザーを検索 */
	public UserDto findByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM users WHERE email = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new UserDto(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"),
						rs.getString("password_hash"));
			}
		}
		return null;
	}

	/* ユーザー情報の更新 */
	public boolean update(UserDto user) throws SQLException {
		String sql = "UPDATE users SET username = ?, email = ?, password_hash = ? WHERE user_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPasswordHash());
			stmt.setInt(4, user.getUserId());
			return stmt.executeUpdate() > 0;
		}
	}

	public boolean insert(UserDto user) throws SQLException {
		String sql = "INSERT INTO users(username,email,password_hash) VALUES (?,?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPasswordHash());
			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;
		}
	}
}
