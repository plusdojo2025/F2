document.addEventListener('DOMContentLoaded', function() {
    var form = document.getElementById('login_form');
    if (form) {
        form.onsubmit = function(event) {
            const email = form.email.value;
            const password = form.password.value;
            if (email === '' || password === '') {
                let output = document.getElementById('output');
                if (!output) {
                    output = document.createElement('div');
                    output.id = 'output';
                    output.style.color = 'red';
                    form.prepend(output);
                }
                output.textContent = 'メールアドレスまたはパスワードが正しくありません。';
                event.preventDefault();
            }
        };
    }
}); 