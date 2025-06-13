<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>議事録の出力</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/download.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
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
  <form class="space-y-6" action="${pageContext.request.contextPath}/download/DownloadService" method="post">
    <!-- 横並び用の flex コンテナ -->
    <div class="form-group flex gap-4 items-end">
      <!-- 会議名検索 -->
      <div class="flex flex-col w-1/3">
        <label for="search-name" class="mb-1">会議名検索</label>
        <input type="text" id="search-name" name="searchName" placeholder="会議名を入力"
          class="p-2 border rounded" />
      </div>

      <!-- 日付検索 -->
      <div class="flex flex-col w-1/3">
        <label for="search-date" class="mb-1">日付検索</label>
        <input type="date" id="search-date" name="searchDate" placeholder="年/月/日"
          class="p-2 border rounded" />
      </div>

      <!-- 会議選択 -->
      <div class="flex flex-col w-1/3">
        <label for="meeting-select" class="mb-1">出力する会議を選択</label>
        <select id="meeting-select" name="meeting" class="p-2 border rounded">
          <option value="">-- 会議を選択してください --</option>
          <%-- ここに動的な会議リストを入れる --%>
        </select>
      </div>
    </div>

    <!-- 出力形式ラジオボタン -->
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

    <!-- ボタン -->
    <div class="flex justify-end">
      <button type="submit" class="bg-emerald-500 hover:bg-emerald-600 text-white px-6 py-3 rounded-full font-semibold shadow-md transition">
        出力する
      </button>
    </div>
  </form>
</main>

  <!-- フッター -->
  <footer>
    &copy; 2025 えんじにっち. All rights reserved.
  </footer>
</body>
</html>