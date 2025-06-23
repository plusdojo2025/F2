<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>エラー | ぎじろくん</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: url('${pageContext.request.contextPath}/assets/images/noise.png'), linear-gradient(135deg, #a2f4ff, #f5feff);
      background-blend-mode: soft-light;
      background-size: cover;
      font-family: 'Plus Jakarta Sans', 'Noto Sans JP', sans-serif;
      color: #047857;
    }
    .error-container {
      background: rgba(255, 255, 255, 0.95);
      padding: 3rem;
      border-radius: 1.5rem;
      text-align: center;
      box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
      max-width: 500px;
      width: 90%;
    }
    .error-container h1 {
      font-size: 2rem;
      margin-bottom: 1rem;
      color: #ef5350;
    }
    .error-container p {
      margin-bottom: 2rem;
    }
    .error-container a {
      display: inline-block;
      background-color: #10b981;
      color: white;
      text-decoration: none;
      padding: 0.75rem 2rem;
      border-radius: 9999px;
      font-weight: bold;
      transition: background-color 0.3s ease;
    }
    .error-container a:hover {
      background-color: #0d9e6b;
    }
  </style>
</head>
<body>
  <div class="error-container">
    <h1>⚠ エラーが発生しました</h1>
    <p>
      <%= request.getAttribute("error") != null ? request.getAttribute("error") : "予期せぬエラーが発生しました。" %>
    </p>
    <a href="${pageContext.request.contextPath}/dashboard">ダッシュボードへ戻る</a>
  </div>
</body>
</html> 