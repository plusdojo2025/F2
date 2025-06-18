document.addEventListener('DOMContentLoaded', function() {
    var form = document.getElementById('login_form');
    if (form) {
        form.onsubmit = function(event) {
            const email = form.email.value.trim();
            const password = form.password.value;
            
            // 入力値チェック
            if (email === '' || password === '') {
                let output = document.getElementById('output');
                if (!output) {
                    output = document.createElement('div');
                    output.id = 'output';
                    output.style.color = 'red';
                    output.style.textAlign = 'center';
                    output.style.marginBottom = '10px';
                    form.prepend(output);
                }
                output.textContent = 'メールアドレスとパスワードを入力してください。';
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