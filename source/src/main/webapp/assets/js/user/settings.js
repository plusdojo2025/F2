document.addEventListener('DOMContentLoaded', () => {
	// 1. サーバー側からのエラーメッセージを画面に表示
	const errorElem = document.getElementById('error-message');
	if (errorElem && errorElem.textContent.trim() !== '') {
		errorElem.style.display = 'block';  // 非表示解除
	}

	// 2. パスワード確認欄のチェック（送信前）
	const form = document.querySelector('.account-form');
	form.addEventListener('submit', (e) => {
		const newPassword = document.getElementById('new-password').value;
		const confirmPassword = document.getElementById('confirm-password').value;
		const errorMessage = document.getElementById('error-message');

		if (newPassword !== confirmPassword) {
			e.preventDefault();  // フォーム送信を止める
			if (errorMessage) {
				errorMessage.textContent = '新しいパスワードが一致しません。';
				errorMessage.style.display = 'block';
			} else {
				alert('新しいパスワードが一致しません。');
			}
		}
	});
});

// パスワードの表示・非表示切替関数（JSPのonclickで呼ばれている関数）
function togglePassword(inputId, button) {
	const input = document.getElementById(inputId);
	if (input.type === 'password') {
		input.type = 'text';
		button.textContent = '🐵';  // 見える状態のアイコンに切替
	} else {
		input.type = 'password';
		button.textContent = '🙈';  // 見えない状態のアイコンに切替
	}
}
