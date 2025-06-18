package model.dto;

public class UserDto {

	private int userId;
	private String username;
	private String email;
	private String passwordHash;

	public UserDto() {
	}

	public UserDto(int userId, String username, String email, String passwordHash) {
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	// アカウント設定用：ユーザーがフォームで入力した「現在のパスワード」
	private String currentPassword;

	// アカウント設定用：ユーザーがフォームで入力した「新しいパスワード（ハッシュ前）」
	private String newPassword;

	// ゲッター・セッターを追加
	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
