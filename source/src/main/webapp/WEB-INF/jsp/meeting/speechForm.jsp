<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>ぎじろくん - 発言入力</title>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/speech.css">
  <script src="<%=request.getContextPath()%>/assets/js/meeting/sppech.js" defer></script>
</head>
<body>

  <!-- ヘッダー -->
  	<header class="header">
	  <h1 class="logo">📝 ぎじろくん</h1>
	  <nav class="nav">
	    <a href="${pageContext.request.contextPath}/dashboard">ホーム</a>
	    <a href="${pageContext.request.contextPath}/meeting/list">会議一覧</a>
	    <a href="${pageContext.request.contextPath}/meeting/create">会議作成</a>
	    <a href="${pageContext.request.contextPath}/download/preview">出力</a>
	    <a href="${pageContext.request.contextPath}/settings">アカウント設定</a>
	  </nav>
	</header>

  <!-- メイン -->
  <main>
    <div class="main-content">
      <div class="glass" style="max-width: 720px; margin: 2rem auto; padding: 2rem;">
        <h2 class="page-title">📝 発言・決定事項入力</h2>

        <form action="speech" method="POST" class="form-block">
          <div class="form-group">
            <label for="title">議題</label>
            <input type="text" id="title" name="title" placeholder="例：開発進捗確認" class="form-input">
          </div>

          <div class="sub-section">
            <div class="form-group">
              <label for="speech">発言内容</label>
              <textarea id="speech" name="speech" class="form-input" rows="3" placeholder="発言者: 内容を入力してください"></textarea>
            </div>

            <div class="form-group">
              <label for="decision">決定事項</label>
              <textarea id="decision" name="decision" class="form-input" rows="3" placeholder="この議題で決まった内容を記載してください"></textarea>
            </div>
          </div>

          <div class="buttons">
            <button type="reset" class="btn-delete">リセット</button>
            <button type="button" class="btn-edit">議題を追加</button>
            <button type="submit" class="btn-detail">次へ（プレビュー）</button>
          </div>
        </form>
      </div>
    </div>
  </main>

  <!-- フッター -->
  <footer>
    &copy; 2025 えんじにっち. All rights reserved.
  </footer>

</body>
</html>
