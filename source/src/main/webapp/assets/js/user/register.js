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
				let output = document.getElementById('output');
				if (!output) {
					output = document.createElement('div');
					output.id = 'output';
					output.style.color = 'red';
					output.style.textAlign = 'center';
					output.style.marginBottom = '10px';
					form.prepend(output);
				}
				output.textContent = 'すべての項目を入力してください。';
				event.preventDefault();
				return;
			}
			
			// パスワード一致チェック
			if (password !== passwordConfirm) {
				let output = document.getElementById('output');
				if (!output) {
					output = document.createElement('div');
					output.id = 'output';
					output.style.color = 'red';
					output.style.textAlign = 'center';
					output.style.marginBottom = '10px';
					form.prepend(output);
				}
				output.textContent = 'パスワードが一致しません。';
				event.preventDefault();
				return;
			}
			
			// パスワードの複雑性チェック（8文字以上、英字と数字を含む）
			const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d).{8,}$/;
			if (!passwordPattern.test(password)) {
				let output = document.getElementById('output');
				if (!output) {
					output = document.createElement('div');
					output.id = 'output';
					output.style.color = 'red';
					output.style.textAlign = 'center';
					output.style.marginBottom = '10px';
					form.prepend(output);
				}
				output.textContent = 'パスワードは8文字以上で、英字と数字を含めてください。';
				event.preventDefault();
				return;
			}
			
			// メールアドレス形式チェック
			const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
			if (!emailPattern.test(email)) {
				let output = document.getElementById('output');
				if (!output) {
					output = document.createElement('div');
					output.id = 'output';
					output.style.color = 'red';
					output.style.textAlign = 'center';
					output.style.marginBottom = '10px';
					form.prepend(output);
				}
				output.textContent = '正しいメールアドレス形式で入力してください。';
				event.preventDefault();
				return;
			}
			
			// エラーメッセージをクリア
			const output = document.getElementById('output');
			if (output) {
				output.remove();
			}
		};
	}
});

/*パスワードを入力時表示or非表示*/

window.addEventListener ('DOMCContentLoaded'), function() {
	let passwordBtn = document.getElementById("passwordBtn");
	let password = document.getElementById("password");
	
	passwordBtn.addEventListener("click",(e)=> {
	
	
		e.preventDefault();
		
		if(password.type ==='password')	{
			password.type = 'text';
			passwordBtn.textContent = '🐵';
		} else {
			password.type = 'password';
			passwordBtn.textContent = '🙈';
		}
	});


}
 
/*const p = document.getElementById('password'); p.type = p.type === 'password' ? 'text' : 'password'; this.textContent = p.type === 'password' ? '🙈' : '🐵'; */





 