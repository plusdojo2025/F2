<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" %>
<c:set var="user" value="${sessionScope.loginUser}" />
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>ダッシュボード | ぎじろくん</title>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/dashboard.css">
  <script src="<%=request.getContextPath()%>/assets/js/dashboard.js" defer></script>
</head>
<body>
  <!-- ヘッダー -->
  <header class="header glass dashboard-header">
    <span class="logo">📝 ぎじろくん</span>
    <h1 class="dashboard-title">📊 ダッシュボード</h1>
    <a href="#" class="logout-btn" onclick="openModal(); return false;">ログアウト</a>
  </header>

  <!-- メイン -->
  <main>
    <h2 class="page-title">ようこそ、<c:out value="${user.username}" /> さん！</h2>

    <div class="card-grid">
      <a href="<%=request.getContextPath()%>/meeting/list" class="card">
        <h3>📂 会議一覧</h3>
        <p>過去の会議を確認・検索できます</p>
      </a>
      <a href="<%=request.getContextPath()%>/meeting/create" class="card">
        <h3>📂 会議作成</h3>
        <p>新しい会議を登録して記録を始めましょう</p>
      </a>
      <a href="<%=request.getContextPath()%>/download" class="card">
        <h3>📤 出力</h3>
        <p>議事録をPDFやTextで出力できます</p>
      </a>
      <a href="<%=request.getContextPath()%>/settings" class="card">
        <h3>👤 アカウント設定</h3>
        <p>プロフィールやパスワードの変更</p>
      </a>
    </div>

    <!-- カレンダー表示エリア -->
    <div id="calendar" class="calendar-container">
      カレンダーを読み込み中...
    </div>
  </main>

  <!-- フッター -->
  <footer>
    &copy; 2025 Gijirokun. All rights reserved.
  </footer>

  <!-- ログアウト確認モーダル -->
  <div id="logoutModal" class="modal hidden">
    <div class="modal-content">
      <p>本当にログアウトしてもよろしいですか？</p>
      <div class="modal-buttons">
        <button onclick="closeModal()">キャンセル</button>
        <a href="<%=request.getContextPath()%>/logout" class="confirm-btn">ログアウト</a>
      </div>
    </div>
  </div>
</body>
</html>
