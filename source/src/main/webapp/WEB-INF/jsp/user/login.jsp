<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang ="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>ログイン | ぎじろくん</title>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap" rel = "stylesheet" >
<link rel = "stylesheet" href ="<%=request.getContextPath()%>/assets/css/login.css">
</head>
<body>
<main class = "main-container">
		<div class = "glass">
		<h1 class = "page-title">🔐 ログイン</h1>

<!-- ログインフォーム -->
<form class = "form-block" id = "login_form" method="POST" action="<%= request.getContextPath() %>/login">
<div class = "form-group">	
	<label for="email" >メールアドレス</label>
	<input type = "email"  id = "email" name = "email" required class = "form-input">
</div>
<div class = "form-group">
	<label for = "password" >パスワード</label>
	<input type = "password" id = "password" name = "password" required class ="form-input">
</div>	
<button type ="submit"  class = "btn-detail">ログイン
</button>
</form>
 
 <div class = "center-text">
 アカウントをお持ちでない方は<a href = "<c:url value ='/RegisterServlet'/>" class = "center-link">新規登録</a>
 </div>
</div>
</main>
<!-- フッター -->
<footer>
    <p>&copy;2025 えんじにっち． All rights reserved.</p>
</footer>
<!-- JSファイルの読み込みはbodyの最後で -->
<script src="${pageContext.request.contextPath}/assets/js/user/login.js"></script>
</body>
</html>




