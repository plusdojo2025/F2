document.addEventListener('DOMContentLoaded', function() {
	var form = document.getElementById('register_form');
	if (form) {
		form.onsubmit = function(event) {
			const username = form.username.value.trim();
			const email = form.email.value.trim();
			const password = form.password.value;
			const passwordConfirm = form.password_confirm.value;
			
			// 入力値チェック
			if (username === '' || email === '' || password === '' || passwordConfirm === '') {
				showNotification('すべての項目を入力してください。', 'error');
				event.preventDefault();
				return;
			}
			
			// パスワード一致チェック
			if (password !== passwordConfirm) {
				showNotification('パスワードが一致しません。', 'error');
				event.preventDefault();
				return;
			}
			
			// パスワードの複雑性チェック（8文字以上、英字と数字を含む）
			const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d).{8,}$/;
			if (!passwordPattern.test(password)) {
				showNotification('パスワードは8文字以上で、英字と数字を含めてください。', 'error');
				event.preventDefault();
				return;
			}
			
			// メールアドレス形式チェック
			const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
			if (!emailPattern.test(email)) {
				showNotification('正しいメールアドレス形式で入力してください。', 'error');
				event.preventDefault();
				return;
			}
			
			// バリデーション成功時は通知をクリア
			clearNotification();
			
			// 登録処理中の表示
			showNotification('登録処理中...', 'info');
			
			// フォーム送信を許可（サーバーサイドで処理）
		};
	}
	
	// ページ読み込み時に成功メッセージがあるかチェック
	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams.get('registered') === 'true') {
		showNotification('登録が完了しました！ログインしてください。', 'success');
	}
});

// 通知表示関数
function showNotification(message, type = 'info') {
	// 既存の通知をクリア
	clearNotification();
	
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
	
	// 自動で消える（成功・エラーの場合）
	if (type === 'success' || type === 'error') {
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
}

// 通知クリア関数
function clearNotification() {
	const existingNotification = document.getElementById('notification');
	if (existingNotification) {
		existingNotification.remove();
	}
}

// パスワード表示切り替え関数
function togglePassword(inputId, button) {
	const input = document.getElementById(inputId);
	if (input.type === 'password') {
		input.type = 'text';
		button.textContent = '🐵';
	} else {
		input.type = 'password';
		button.textContent = '🙈';
	}
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
 