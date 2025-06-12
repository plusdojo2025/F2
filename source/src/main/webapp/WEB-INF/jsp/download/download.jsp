<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>ユーザー設定</title>
</head>
<body>
  <!-- 共通ヘッダー -->
  <header class="glass flex justify-between items-center p-6 mb-10 mt-10 max-w-7xl mx-auto">
  <h1 class="text-3xl font-extrabold tracking-wide text-emerald-700 drop-shadow-md">🗒️ 議事録アプリ</h1>
  <nav class="flex gap-8 text-lg font-semibold text-emerald-700">
    <a href="${pageContext.request.contextPath}/dashboard" class="hover:text-emerald-900 transition">ホーム</a>
    <a href="${pageContext.request.contextPath}/meeting/search" class="hover:text-emerald-900 transition">会議一覧</a>
    <a href="${pageContext.request.contextPath}/meeting/create" class="hover:text-emerald-900 transition">会議作成</a>
    <a href="${pageContext.request.contextPath}/download/preview" class="hover:text-emerald-900 transition">出力</a>
    <a href="${pageContext.request.contextPath}/settings" class="hover:text-emerald-900 transition">アカウント設定</a>
  </nav>
</header>

  
      <h2 class="text-4xl font-bold text-emerald-900 mb-10 text-center drop-shadow">🖨️ 議事録の出力</h2>


  <!-- 出力画面 -->
  <main>
    <form class="space-y-6">
      <div class="form-group">
        <label for="search">会議名検索</label>
        <input type="text" id="search" name="search" placeholder="会議名を入力" />
        <label for="search">日付検索</label>
        <input type="text" id="search" name="search" placeholder="年/月/日" />
        <label for="meeting-select">出力する会議を選択</label>
        <select id="meeting-select" name="meeting" class="w-full mt-1">
          <option value="">-- 会議を選択してください --</option>
          <option value="1">開発定例ミーティング（2025-06-01）</option>
          <option value="2">設計レビュー（2025-06-03）</option>
        </select>
      </div>
      <div class="form-group">
        <label class="block mb-1">出力形式</label>
        <div class="flex gap-6">
          <label class="radio-label">
            <input type="radio" name="format" value="pdf" checked>
            <span>PDF</span>
          </label>
          <label class="radio-label">
            <input type="radio" name="format" value="text">
            <span>Text</span>
          </label>
        </div>
      </div>
      <div class="flex justify-end">
        <button type="submit">出力する</button>
      </div>
    </form>
  </main>

  <!-- フッター -->
  <footer>
    &copy; 2025 えんじにっち. All rights reserved.
  </footer>
</body>
</html>