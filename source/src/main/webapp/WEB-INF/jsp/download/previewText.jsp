<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  String previewText = (String) request.getAttribute("textPreview");
  String meetingId = (String) request.getAttribute("meetingId");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>テキストプレビュー | ぎじろくん</title>
  <style>
    body {
      font-family: 'Courier New', monospace;
      background-color: #f9f9f9;
      padding: 40px;
      color: #333;
    }
    h1 {
      text-align: center;
      color: #065f46;
      font-size: 1.8rem;
      margin-bottom: 30px;
    }
    pre {
      background-color: #fff;
      padding: 24px;
      border: 1px solid #ccc;
      border-radius: 8px;
      white-space: pre-wrap;
      word-wrap: break-word;
      overflow-x: auto;
    }
    .actions {
      margin-top: 30px;
      text-align: center;
    }
    .actions form {
      display: inline-block;
      margin-right: 10px;
    }
    .actions button, .actions a {
      background-color: #065f46;
      color: white;
      border: none;
      padding: 10px 20px;
      text-decoration: none;
      font-size: 1rem;
      border-radius: 6px;
      cursor: pointer;
    }
    .actions a {
      background-color: #6b7280;
    }
  </style>
</head>
<body>

  <h1>🖨️ テキスト議事録プレビュー</h1>

  <pre><%= previewText %></pre>

  <div class="actions">
    <form method="post" action="${pageContext.request.contextPath}/preview">
      <input type="hidden" name="meetingId" value="<%= meetingId %>" />
      <input type="hidden" name="format" value="text" />
      <button type="submit">この内容で出力する</button>
    </form>
    <a href="${pageContext.request.contextPath}/download">戻る</a>
  </div>

</body>
</html>
