<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ユーザー設定 | ぎじろくん</title>
<link
	href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/css/Style.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/css/settings.css">
<script src="<%=request.getContextPath()%>/assets/js/user/settings.js" defer></script>
</head>
<body>

	<!-- 共通ヘッダー -->
	<header class="header">
		<h1 class="logo">📝 ぎじろくん</h1>
		<nav class="nav">
			<a href="${pageContext.request.contextPath}/dashboard">ホーム</a> <a
				href="${pageContext.request.contextPath}/meeting/list">会議一覧</a> <a
				href="${pageContext.request.contextPath}/meeting/create">会議作成</a> <a
				href="${pageContext.request.contextPath}/download">出力</a> <a
				href="${pageContext.request.contextPath}/settings">アカウント設定</a>
		</nav>
	</header>
	<h2>👤 ユーザー設定</h2>

	<main>
		<!-- エラーメッセージ表示用 -->
		<c:if test="${not empty error}">
			<div id="error-message" class="error-message">${error}</div>
		</c:if>


		<form class="account-form"
			action="${pageContext.request.contextPath}/settings" method="post">
			<div class="form-group">
				<label for="username">ユーザー名</label> <input type="text" id="username"
					name="username" value="${user.username}" required>
			</div>

			<div class="form-group">
				<label for="email">メールアドレス</label> <input type="email" id="email"
					name="email" value="${user.email}" required>
			</div>

			<div class="form-group">
				<label for="current-password">現在のパスワード</label> <input
					type="password" id="current-password" name="currentPassword"
					required>
			</div>

			<div class="form-group password-with-toggle">
				<label for="new-password">新しいパスワード</label>
				<div class="input-with-icon">
					<input type="password" id="new-password" name="newPassword">
					<button type="button" class="password-toggle"
						onclick="togglePassword('new-password', this)">🙈</button>
				</div>
			</div>

			<div class="form-group">
				<label for="confirm-password">パスワード（確認）</label> <input
					type="password" id="confirm-password" name="confirmPassword">
			</div>

			<div class="form-actions">
				<button type="submit">変更を保存</button>
			</div>
		</form>
	</main>

	<!-- フッター -->
	<footer> &copy; 2025 えんじにっち. All rights reserved. </footer>

</body>
</html>
