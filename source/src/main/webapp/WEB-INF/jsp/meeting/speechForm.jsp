<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%
  // GETパラメータの meetingId を取得（初期表示で使う）
  String meetingId = request.getParameter("meetingId");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>ぎじろくん - 発言入力</title>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/speech.css">
  <script src="<%=request.getContextPath()%>/assets/js/meeting/speech.js" defer></script>
</head>
<body>

  <!-- ヘッダー -->
  <header class="header">
    <h1 class="logo">📘 ぎじろくん</h1>
    <nav class="nav">
      <a href="${pageContext.request.contextPath}/dashboard">ホーム</a>
      <a href="${pageContext.request.contextPath}/meeting/list">会議一覧</a>
      <a href="${pageContext.request.contextPath}/meeting/create">会議作成</a>
      <a href="${pageContext.request.contextPath}/download">出力</a>
      <a href="${pageContext.request.contextPath}/user/settings">アカウント設定</a>
    </nav>
  </header>

  <!-- メイン -->
  <main class="main-container">
    <h2 class="page-title">発言・決定事項入力</h2>

    <form method="post" action="${pageContext.request.contextPath}/speech/register">
      <input type="hidden" name="meetingId" value="${meeting.meetingId}" />

      <div id="agenda-forms-container">
        <div class="agenda-block" data-agenda-index="0">
          <div class="form-group">
            <label for="agenda0-title">議題</label>
            <input type="text" id="agenda0-title" name="agendas[0].title" placeholder="例：開発進捗確認">
          </div>
          <div class="form-group">
            <label for="agenda0-speech">発言内容</label>
            <textarea id="agenda0-speech" name="agendas[0].speechNote" rows="5" placeholder="発言者: 内容を入力してください"></textarea>
          </div>
          <div class="form-group">
            <label for="agenda0-decision">決定事項</label>
            <textarea id="agenda0-decision" name="agendas[0].decisionNote" rows="3" placeholder="この議題で決まった内容を記載してください"></textarea>
          </div>
        </div>
      </div>

      <!-- JSで追加される議題テンプレート -->
      <template id="agenda-form-template">
        <div class="agenda-block" data-agenda-index="PLACEHOLDER_INDEX">
           <hr style="margin: 2rem 0; border-color: #99f6e4;">
          <div class="form-group">
            <label for="agendaPLACEHOLDER_INDEX-title">議題</label>
            <input type="text" id="agendaPLACEHOLDER_INDEX-title" name="agendas[PLACEHOLDER_INDEX].title" placeholder="例：開発進捗確認">
          </div>
          <div class.form-group">
            <label for="agendaPLACEHOLDER_INDEX-speech">発言内容</label>
            <textarea id="agendaPLACEHOLDER_INDEX-speech" name="agendas[PLACEHOLDER_INDEX].speechNote" rows="5" placeholder="発言者: 内容を入力してください"></textarea>
          </div>
          <div class="form-group">
            <label for="agendaPLACEHOLDER_INDEX-decision">決定事項</label>
            <textarea id="agendaPLACEHOLDER_INDEX-decision" name="agendas[PLACEHOLDER_INDEX].decisionNote" rows="3" placeholder="この議題で決まった内容を記載してください"></textarea>
          </div>
           <div class="buttons" style="justify-content: flex-end;">
             <button type="button" class="btn-delete delete-agenda-button">この議題を削除</button>
           </div>
        </div>
      </template>

      <div class="buttons">
        <button type="button" class="btn-edit" id="add-agenda-button">議題を追加</button>
        <button type="reset" class="btn-delete">リセット</button>
        <button type="submit" name="action" value="preview" class="btn-preview" formaction="${pageContext.request.contextPath}/meeting/preview">プレビューで確認</button>
      </div>
    </form>
  </main>

  <!-- フッター -->
  <footer>&copy; 2025 えんじにっち. All rights reserved.</footer>

  <!-- リセット確認モーダル -->
  <div id="resetModal" class="modal hidden">
    <div class="modal-content">
      <p>本当にリセットしますか？</p>
      <div class="modal-buttons">
        <button id="confirmReset">はい</button>
        <button id="cancelReset">キャンセル</button>
      </div>
    </div>
  </div>

  <!-- 議題追加確認モーダル -->
  <div id="addModal" class="modal hidden">
    <div class="modal-content">
      <p>議題を追加しますか？</p>
      <div class="modal-buttons">
        <button id="confirmAdd">はい</button>
        <button id="cancelAdd">キャンセル</button>
      </div>
    </div>
  </div>

</body>
</html>
