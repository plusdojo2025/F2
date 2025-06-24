<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>メール認証 - ぎじろくん</title>
    <style>
      html, body {
        margin: 0;
        padding: 0;
        min-height: 100vh;
        font-family: 'Plus Jakarta Sans', 'Noto Sans JP', sans-serif;
        background: linear-gradient(135deg, #A2F4FF, #F5FEFF);
        background-blend-mode: soft-light;
        background-repeat: no-repeat;
        background-size: cover;
        color: #065F46;
        box-sizing: border-box;
      }
      .main-container {
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 1rem;
        background: #f0fdfa;
      }
      .glass {
        width: 100%;
        max-width: 28rem;
        padding: 2rem;
        background: rgba(255, 255, 255, 0.8);
        border-radius: 1rem;
        box-shadow: 0 8px 32px rgba(0,0,0,0.1);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
        animation: fadeIn 0.8s ease forwards;
      }
      @keyframes fadeIn {
        from {opacity: 0; transform: translateY(10px);}
        to {opacity: 1; transform: translateY(0);}
      }
      .page-title {
        font-size: 1.875rem;
        font-weight: 700;
        text-align: center;
        color: #065f46;
        text-shadow: 0 1px 2px rgba(0,0,0,0.15);
      }
      .form-block {
        display: flex;
        flex-direction: column;
        gap: 1.25rem;
      }
      .form-group {
        display: flex;
        flex-direction: column;
      }
      .form-group label {
        font-size: 0.875rem;
        font-weight: 500;
        color: #064e3b;
        margin-bottom: 0.25rem;
      }
      .form-input {
        width: 95%;
        margin-top: 0.25rem;
        border: 1px solid #a7f3d0;
        border-radius: 0.75rem;
        padding: 0.75rem;
        background-color: rgba(255, 255, 255, 0.8);
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
        font-size: 1rem;
        transition: border-color 0.3s ease, box-shadow 0.3s ease;
      }
      .form-input:focus {
        outline: none;
        border-color: #34d399;
        box-shadow: 0 0 0 2px #34d399;
      }
      .btn-detail {
        background-color: #10b981;
        color: white;
        padding: 0.75rem 0;
        font-weight: 600;
        border-radius: 9999px;
        box-shadow: 0 4px 6px rgba(16, 185, 129, 0.4);
        border: none;
        cursor: pointer;
        font-size: 1rem;
        transition: background-color 0.3s ease;
        width: 100%;
      }
      .btn-detail:hover {
        background-color: #059669;
      }
      .center-text {
        text-align: center;
        font-size: 0.875rem;
        color: #065f46;
      }
      .center-link {
        color: #059669;
        text-decoration: underline;
        transition: color 0.3s ease;
      }
      .center-link:hover {
        color: #065f46;
      }
      /* ステップインジケーター用 */
      .step {
        background: #d1fae5;
        color: #065f46;
        border-radius: 50%;
        width: 30px;
        height: 30px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: bold;
      }
      .step.active {
        background: #10b981;
        color: white;
      }
    </style>
</head>
<body>
<main class="main-container">
  <div class="glass">
    <h1 class="page-title">メール認証</h1>
    <!-- ステップインジケーター -->
    <div style="display:flex; justify-content:center; margin-bottom:20px;">
      <div class="step active">1</div>
      <div style="width:40px; height:2px; background:#10b981; margin:15px 0;"></div>
      <div class="step" id="step2">2</div>
    </div>
    <!-- エラーメッセージ表示 -->
    <c:if test="${not empty errorMessage}">
        <div style="color:red; text-align:center; margin-bottom:10px;">${errorMessage}</div>
    </c:if>
    <!-- 成功メッセージ表示 -->
    <c:if test="${not empty successMessage}">
        <div style="color:green; text-align:center; margin-bottom:10px;">${successMessage}</div>
    </c:if>
    <!-- 認証コード表示（シンプル表示） -->
    <c:if test="${not empty verificationCode}">
        <div style="background:#e8f5e8; border:2px solid #28a745; border-radius:8px; padding:15px; margin-bottom:15px; text-align:center;">
            <strong style="color:#155724; font-size:16px;">認証コード: ${verificationCode}</strong>
        </div>
    </c:if>
    <!-- ステップ1: メールアドレス入力 -->
    <c:choose>
      <c:when test="${empty email}">
        <div class="form-section active" id="step1-section">
            <form class="form-block" method="post" action="${pageContext.request.contextPath}/mail-confirm">
                <input type="hidden" name="action" value="send-code">
                <div class="form-group">
                    <label for="email">メールアドレス</label>
                    <input type="email" id="email" name="email" value="${email}" required class="form-input">
                </div>
                <button type="submit" class="btn-detail">認証コードを送信</button>
            </form>
            <div class="center-text">
              <a href="${pageContext.request.contextPath}/login" class="center-link">ログインページに戻る</a>
            </div>
        </div>
      </c:when>
      <c:otherwise>
        <div class="form-section active" id="step2-section">
            <form class="form-block" method="post" action="${pageContext.request.contextPath}/mail-confirm">
                <input type="hidden" name="action" value="verify-code">
                <div class="form-group">
                    <label for="verificationCode">認証コード（6桁）</label>
                    <input type="text" id="verificationCode" name="verificationCode" maxlength="6" pattern="[0-9]{6}" required placeholder="135790" class="form-input">
                    <small>送信された6桁の数字を入力してください</small>
                </div>
                <button type="submit" class="btn-detail">認証コードを確認</button>
            </form>
            <div class="center-text" style="display: flex; flex-direction: column; gap: 0.5rem; align-items: center;">
              <button type="button" class="btn-detail" style="background:#6c757d; margin-top: 0.5rem; width: 100%;" onclick="resendCode()">認証コードを再送信</button>
              <a href="${pageContext.request.contextPath}/mail-confirm" class="center-link">メールアドレスを変更</a>
            </div>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</main>
<!-- スクリプトはbody直前に配置 -->
<script>
function resendCode() {
    var email = document.getElementsByName('email')[0]?.value || '${email}';
    if (email) {
        var form = document.createElement('form');
        form.method = 'post';
        form.action = '${pageContext.request.contextPath}/mail-confirm';
        var actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = 'send-code';
        var emailInput = document.createElement('input');
        emailInput.type = 'hidden';
        emailInput.name = 'email';
        emailInput.value = email;
        form.appendChild(actionInput);
        form.appendChild(emailInput);
        document.body.appendChild(form);
        form.submit();
    }
}
// 認証コード送信後にステップ2を表示
<c:if test="${not empty email}">
    document.getElementById('step1-section')?.style.display = 'none';
    document.getElementById('step2-section')?.style.display = 'block';
    document.getElementById('step2').classList.add('active');
</c:if>
// 認証コード入力フィールドの制限
if(document.getElementById('verificationCode')) {
    document.getElementById('verificationCode').addEventListener('input', function(e) {
        this.value = this.value.replace(/[^0-9]/g, '');
    });
}
</script>
</body>
</html>