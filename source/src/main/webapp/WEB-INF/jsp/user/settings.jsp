<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/webapp/assets/css/Style.css">
<link rel="stylesheet" href="/webapp/assets/css/settings.css">
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
       <h2 class="text-2xl font-bold text-emerald-800 mb-6 text-center drop-shadow-md">👤 アカウント設定</h2>

<!-- アカウント設定画面 -->
  <main class="px-6 mt-10 max-w-2xl mx-auto pb-20">
    <div class="glass p-8">
      <form class="space-y-6">
        <div>
          <label for="username" class="block text-sm font-medium text-emerald-700">ユーザー名</label>
          <input type="text" id="username" name="username" value="エンジ太郎"
            class="w-full mt-1 border border-emerald-200 rounded-xl p-3 bg-white bg-opacity-80 shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-400" />
        </div>
        <div>
          <label for="email" class="block text-sm font-medium text-emerald-700">メールアドレス</label>
          <input type="email" id="email" name="email" value="user@example.com"
            class="w-full mt-1 border border-emerald-200 rounded-xl p-3 bg-white bg-opacity-80 shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-400" />
        </div>
        <div>
          <label for="password" class="block text-sm font-medium text-emerald-700">現在のパスワード</label>
          <input type="password" id="password" name="password"
            class="w-full mt-1 border border-emerald-200 rounded-xl p-3 bg-white bg-opacity-80 shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-400" />
        </div>
        <div>
          <label for="password" class="block text-sm font-medium text-emerald-700">新しいパスワード</label>
          <input type="password" id="password" name="password"
            class="w-full mt-1 border border-emerald-200 rounded-xl p-3 bg-white bg-opacity-80 shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-400" />
        </div>
        <div>
          <label for="password-confirm" class="block text-sm font-medium text-emerald-700">パスワード（確認）</label>
          <input type="password" id="password-confirm" name="password-confirm"
            class="w-full mt-1 border border-emerald-200 rounded-xl p-3 bg-white bg-opacity-80 shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-400" />
        </div>
        <div class="flex justify-end">
          <button type="submit"
            class="bg-emerald-500 hover:bg-emerald-600 text-white px-6 py-3 rounded-full font-semibold shadow-md transition">
            変更を保存
          </button>
        </div>
      </form>
    </div>
  </main>
<!-- フッター -->
  <footer class="text-center text-sm text-emerald-600 py-6">
    &copy; 2025 えんじにっち. All rights reserved.
  </footer>
</body>
</html>