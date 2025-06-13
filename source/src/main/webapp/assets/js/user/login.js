

document.getElementById('login_form').onsubmit = function(event) {
      const email = document.getElementById('login_form').email.value;
      const password = document.getElementById('login_form').password.value;
      if (email === '' || password === '') {
          document.getElementById('output').textContent = `メールアドレスまたはパスワードが正しくありません。`;
          event.preventDefault();
      }
  }; 