<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  String verifiedEmail = (String) session.getAttribute("verifiedEmail");
  if (verifiedEmail == null || verifiedEmail.isEmpty()) {
    response.sendRedirect(request.getContextPath() + "/mail-confirm");
    return;
  }
%>
<!DOCTYPE html>
<html lang ="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>新規登録 | ぎじろくん</title>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap" rel = "stylesheet" >
<link rel = "stylesheet" href ="<%=request.getContextPath()%>/assets/css/register.css">
</head>
<body>
<main class = "main-container">
		<div class = "glass">
		<h1 class = "page-title">📝 新規登録</h1>

		<!-- エラーメッセージ表示 -->
		<c:if test="${not empty errorMessage}">
		  <div style="color:red; text-align:center; margin-bottom:10px;">
		    ${errorMessage}
		  </div>
		</c:if>
		<!-- 登録完了メッセージ表示（login.jspで受け取る想定） -->
		<c:if test="${param.registered eq 'true'}">
		  <div style="color:green; text-align:center; margin-bottom:10px;">
		    登録が完了しました。ログインしてください。
		  </div>
		</c:if>

<!-- 登録フォーム -->
<form class = "form-block" id = "register_form" method="POST" action="<%= request.getContextPath() %>/register">
<div class = "form-group">	
	<label for="username" >ユーザー名</label>
	<input type = "text"  id = "username" name = "username" required class = "form-input">
</div>
<div class = "form-group">	
	<label for="email" >メールアドレス</label>
	<input type = "email"  id = "email" name = "email" 
		value="${sessionScope.verifiedEmail}" 
		<c:if test='${not empty sessionScope.verifiedEmail}'>readonly</c:if>
		required class = "form-input">
</div>
<div class = "form-group">
	<label for = "password" >パスワード</label>
	<div class="input-with-icon">
		<input type="password" id="password" name="password" required class="form-input">
		<button type="button" class="password-toggle"
			onclick="togglePassword('password', this)">🙈</button>
	</div>
</div>
<div class = "form-group">
	<label for = "password_confirm" >パスワード確認</label>
	<div class="input-with-icon password-confirm-custom">
		<input type="password" id="password_confirm" name="password_confirm" required class="form-input password-confirm-input">
		<button type="button" class="password-toggle"
			onclick="togglePassword('password_confirm', this)">🙈</button>
	</div>
</div>	
<button type ="submit"  class = "btn-detail">登録
</button>
</form>
 
 <div class = "center-text">
 すでにアカウントをお持ちの方は<a href = "<%= request.getContextPath() %>/login" class = "center-link">ログイン</a>
 </div>
</div>
</main>
<!-- フッター -->
<footer>
    <p>&copy;2025 えんじにっち． All rights reserved.</p>
</footer>
<!-- JSファイルの読み込みはbodyの最後で -->
<script src="${pageContext.request.contextPath}/assets/js/user/register.js"></script>
</body>
</html>