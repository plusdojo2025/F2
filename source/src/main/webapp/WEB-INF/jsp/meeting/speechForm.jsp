<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>発言内容登録</title>
</head>
<body>
	<!-- 共通ヘッダー -->
	<header
		class="glass flex justify-between items-center p-6 mb-10 mt-10 max-w-7xl mx-auto">
		<h1
			class="text-3xl font-extrabold tracking-wide text-emerald-700 drop-shadow-md">🗒️
			議事録アプリ</h1>
		<nav class="flex gap-8 text-lg font-semibold text-emerald-700">
			<a href="${pageContext.request.contextPath}/dashboard"
				class="hover:text-emerald-900 transition">ホーム</a> <a
				href="${pageContext.request.contextPath}/meeting/search"
				class="hover:text-emerald-900 transition">会議一覧</a> <a
				href="${pageContext.request.contextPath}/meeting/create"
				class="hover:text-emerald-900 transition">会議作成</a> <a
				href="${pageContext.request.contextPath}/download/preview"
				class="hover:text-emerald-900 transition">出力</a> <a
				href="${pageContext.request.contextPath}/settings"
				class="hover:text-emerald-900 transition">アカウント設定</a>
		</nav>
	</header>
</body>
</html>