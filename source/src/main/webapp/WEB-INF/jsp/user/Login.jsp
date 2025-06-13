<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang ="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>ログイン | ぎじろくん</title>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap" rel = "stylesheet" href ="css/login.css">
<script src="https://cdn.tailwindcss.com"></script>
</head>
<body class = "min-h-screen flex items-center justify-center p-4">

	<div class = "glass w-full max-w-md p-8 space-y-6 animate-fade-in">
		<h1 class = "text-3xl font-bold text-center text-emerald-700 drop-shadow-md">🔐 ログイン</h1>

<!-- ログインフォーム -->
<form class = "space-y-5"method="POST" action="/user/LoginServlet">
<div>	
	<label for="email" class="block text-sm font-medium text-emerald-800">メールアドレス</label>
	<input type = "text"  id = "email" name = "email" required class = "w-full mt-1 border border-emerald-200 rounded-xl p-3 bg-white bg-opacity-80 shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-400">
</div>
<div>
	<label for = "password" class ="block text-sm font-medium text-emerald-800">パスワード</label>
	<input type = "password" id = "password" name = "password" required class ="w-full mt-1 border border-emerald-200 rounded-xl p-3 bg-white bg-opacity-80 shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-400">
</div>	
<button type ="submit" name = "loginBtn" class = "flex justify-center items-center gap-2 w-full bg-emerald-500 hover:bg-emerald-600 text-white py-3 rounded-full font-semibold shadow-md transition">
   <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m0 0v2m0-6a4 4 0 100-8 4 4 0 000 8z" />
        </svg>
        ログイン
</button>
<script src = "login.js"></script>
</form>
 
 <div class = "text-center text-sm text-emerald-700">
 アカウントをお持ちでない方は<a href = "/register.jsp">新規登録</a>
 </div>
</div>


<div>
    <p>&copy;2025 えんじにっち． All rights reserved.</p>
</div>
</body>
</html>




