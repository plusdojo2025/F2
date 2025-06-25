<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

		<!-- エラーメッセージ表示 -->
		<c:if test="${not empty errorMessage}">
		  <div style="color:red; text-align:center; margin-bottom:10px;">
		    ${errorMessage}
		  </div>
		</c:if>

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
 アカウントをお持ちでない方は<a href = "<%= request.getContextPath() %>/register" class = "center-link">新規登録</a>
 </div>
</div>
</main>
<!-- フッター -->
<footer>
    <p>&copy;2025 えんじにっち． All rights reserved.</p>
</footer>
<!-- JSファイルの読み込みはbodyの最後で -->
<script src="${pageContext.request.contextPath}/assets/js/user/login.js"></script>
<script>
// 登録完了時の通知表示
document.addEventListener('DOMContentLoaded', function() {
	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams.get('registered') === 'true') {
		showNotification('登録が完了しました！ログインしてください。', 'success');
	}
});

// 通知表示関数
function showNotification(message, type = 'info') {
	const notification = document.createElement('div');
	notification.id = 'notification';
	notification.className = `notification notification-${type}`;
	notification.textContent = message;
	
	// 通知のスタイル設定
	notification.style.cssText = `
		position: fixed;
		top: 20px;
		left: 50%;
		transform: translateX(-50%);
		padding: 12px 24px;
		border-radius: 8px;
		font-weight: 500;
		z-index: 1000;
		box-shadow: 0 4px 12px rgba(0,0,0,0.15);
		animation: slideDown 0.3s ease-out;
		max-width: 90%;
		text-align: center;
	`;
	
	// タイプ別の色設定
	switch(type) {
		case 'success':
			notification.style.backgroundColor = '#10b981';
			notification.style.color = 'white';
			break;
		case 'error':
			notification.style.backgroundColor = '#ef4444';
			notification.style.color = 'white';
			break;
		case 'info':
			notification.style.backgroundColor = '#3b82f6';
			notification.style.color = 'white';
			break;
		default:
			notification.style.backgroundColor = '#6b7280';
			notification.style.color = 'white';
	}
	
	document.body.appendChild(notification);
	
	// 自動で消える
	setTimeout(() => {
		if (notification.parentNode) {
			notification.style.animation = 'slideUp 0.3s ease-out';
			setTimeout(() => {
				if (notification.parentNode) {
					notification.remove();
				}
			}, 300);
		}
	}, 3000);
}

// CSSアニメーション用のスタイル追加
const style = document.createElement('style');
style.textContent = `
	@keyframes slideDown {
		from {
			opacity: 0;
			transform: translateX(-50%) translateY(-20px);
		}
		to {
			opacity: 1;
			transform: translateX(-50%) translateY(0);
		}
	}
	
	@keyframes slideUp {
		from {
			opacity: 1;
			transform: translateX(-50%) translateY(0);
		}
		to {
			opacity: 0;
			transform: translateX(-50%) translateY(-20px);
		}
	}
`;
document.head.appendChild(style);
</script>
</body>
</html>




