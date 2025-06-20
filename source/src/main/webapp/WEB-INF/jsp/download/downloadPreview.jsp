<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>議事録プレビュー | ぎじろくん</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/preview.css">
</head>
<body>
	<!-- 共通ヘッダー -->
	<header class="header">
		<h1 class="logo">📝 ぎじろくん</h1>
		<nav class="nav">
			<a href="${pageContext.request.contextPath}/dashboard">ホーム</a> <a
				href="${pageContext.request.contextPath}/meeting/list">会議一覧</a> <a
				href="${pageContext.request.contextPath}/meeting/create">会議作成</a> <a
				href="${pageContext.request.contextPath}/download/preview">出力</a> <a
				href="${pageContext.request.contextPath}/settings">アカウント設定</a>
		</nav>
	</header>

	<h2>📄 議事録プレビュー</h2>

	<main>
		<!-- 議事録の表示（形式に応じて切り替え） -->
		<div class="minutes-preview">
			<pre>
				<c:out value="${previewText}" />
			</pre>
		</div>

		<!-- 出力ボタンフォーム -->
		<form action="${pageContext.request.contextPath}/download"
			method="post">
			<input type="hidden" name="meeting" value="${meetingId}" /> <input
				type="hidden" name="format" value="${format}" />

			<button type="submit" class="confirm-button">この内容で出力</button>
		</form>

	</main>

	<!-- フッター -->
	<footer>&copy; 2025 えんじにっち. All rights reserved.</footer>
</body>
</html>
