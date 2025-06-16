

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





 