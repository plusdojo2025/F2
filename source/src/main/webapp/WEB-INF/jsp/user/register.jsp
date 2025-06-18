<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>新規登録 | ぎじろくん</title>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet" >
 <link rel = "stylesheet" href = "<%=request.getContextPath()%>/assets/css/register.css">
</head>
<body>

<main>
<div class = "glass">
	 <h1 class = "page-title">📘 新規登録</h1>
	 
<!-- 登録フォーム -->
<form class = "form-block" action ="<%= request.getContextPath() %>/register" method ="POST">

	<div class = "form-group">
	 <label for = "username" >氏名</label>
	 <input type = "text" name = "username" id = "username" required class ="form-input" /> 
	</div>
	
	<div class = "form-group">
	 <label for ="email">メールアドレス</label>
	 <input type = "email" name = "email" id = "email" required class ="form-input">
	</div>
	
	<div class = "form-group">
	<label for = "password" >パスワード</label>
	<input type = "password" name ="password" id ="password" required class = "form-input">
	<button type = "button"  id = "passwordBtn" aria-label ="パスワード表示切替" class = "absolute right-3 top-10 text-emerald-600">
	🙈
    </button>
    </div>
    <div class = "buttons">
    	<button type = "submit" class = "btn-detail">✅アカウント作成</button>
    	
    <script src = "js/register.js"></script>
	</div>
	
	
</form>

	<div class ="center-text">
	  すでにアカウントをお持ちの方は<a href ="/loginServlet" class = "center-link">ログイン</a>
	</div>
</div>
</main>


<!-- フッター -->
<footer>
&copy; 2025 えんじにっち. All rights reserved.
</footer>

</body> 
</html>