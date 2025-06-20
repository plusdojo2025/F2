<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>議事録の出力</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/download.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/style.css">
<script
	src="${pageContext.request.contextPath}/assets/js/download/download.js"
	defer></script>
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
	<h2
		class="text-4xl font-bold text-emerald-900 mb-10 text-center drop-shadow">🖨️
		議事録の出力</h2>

	<!-- 出力画面 -->
	<main>
		<form class="space-y-6"
			action="${pageContext.request.contextPath}/download/preview"
			method="post">

			<!-- 横並び用の flex コンテナ -->
			<div class="form-group flex gap-4 items-end">
				<!-- 会議名検索 -->
				<div class="search-box">
					<label for="search-name" class="mb-1">会議名検索</label> <input
						type="text" id="search-name" name="searchName"
						placeholder="会議名を入力" class="p-2 border rounded" />
				</div>

				<!-- 日付検索 -->
				<div class="search-box">
					<label for="search-date" class="mb-1">日付検索</label> <input
						type="date" id="search-date" name="searchDate" placeholder="yyyy/mm/dd"
						class="p-2 border rounded" />
				</div>

				<!-- 会議選択 -->
				<div class="search-box">
					<label for="meeting-select" class="mb-1">出力する会議を選択</label> <select
						id="meeting-select" name="meeting" class="p-2 border rounded">
						<option value="">-- 会議を選択してください --</option>
						<%-- ここに動的な会議リストを入れる --%>
					</select>
				</div>
			</div>

			<!-- 出力形式ラジオボタン -->
			<!-- ラジオ＋ボタンをまとめて囲む -->
			<div class="download-actions">
			<p class="radio-title">出力形式</p>
				<div class="radio-group">
					<label>
					<input type="radio"name="format" value="pdf"> PDF</label>
					<label>
					<input type="radio" name="format" value="text"checked> TEXT</label> 
				</div>

				<button type="submit" class="download-button">出力する</button>
			</div>


		</form>
	</main>

	<!-- フッター -->
	<footer> &copy; 2025 えんじにっち. All rights reserved. </footer>
</body>
</html>