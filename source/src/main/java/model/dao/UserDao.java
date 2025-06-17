package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.dto.UserDto;


public class UserDao {

	/*db接続*/
	private static final String URL = "jdbc:mysql://localhost:3306/f2";
	private static final String USER = "root";
	private static final String PASS = "password";
	
	/*ユーザー登録*/
	public boolean registerUser (UserDto user) {
		String sql = "INSERT INTO users(username,email,password) VALUES (?,?,?)" ;
		try (Connection conn = DriverManager.getConnection(URL,USER,PASS); 
				PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setString(1,user.getUsername());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPassword());
			
			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		}
		
	}
	
	/*ログイン認証（メールとパスワードが一致するかチェック）*/
	public UserDto findByEmailAndPassword(String email,String password) {
		String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
		try (Connection conn = DriverManager.getConnection(URL,USER,PASS); 
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, email);
			stmt.setString(2, password);
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				
				return new UserDto(
						rs.getInt("user_id"),
						rs.getString("username"),
						rs.getString("email"),
						rs.getString("password")
				);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
