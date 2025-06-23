<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="meeting" value="${meeting}" />

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>${fn:escapeXml(meeting.title)}｜PDFプレビュー | ぎじろくん</title>
  <style>
    body {
      font-family: 'Noto Sans JP', sans-serif;
      background: #e0faff;
      padding: 60px;
      color: #333;
    }
    h1 {
      text-align: center;
      color: #9c4221;
      font-size: 2.5rem;
      border-bottom: 3px solid #facc15;
      padding-bottom: 14px;
      margin-bottom: 40px;
    }
    .info-box {
      max-width: 700px;
      margin: 0 auto 40px;
      border: 2px solid #facc15;
      border-radius: 12px;
      background-color: #fffdfa;
      padding: 24px 32px;
    }
    .info-box table {
      width: 100%;
      border-collapse: collapse;
    }
    .info-box td {
      padding: 8px 12px;
      font-size: 1rem;
    }
    .info-box td.label {
      font-weight: bold;
      width: 25%;
      color: #333;
    }
    .agenda-box {
      border: 1px dashed #facc15;
      border-radius: 10px;
      padding: 20px;
      margin: 40px auto;
      background-color: #ffffff;
      max-width: 700px;
    }
    .agenda-title {
      font-weight: 700;
      font-size: 1.2rem;
      color: #9c4221;
      margin-bottom: 14px;
      border-bottom: 1px solid #ccc;
      padding-bottom: 6px;
    }
    .section-label {
      font-weight: bold;
      color: #b45309;
      margin-top: 14px;
    }
    .section-content {
      margin-top: 6px;
      background: #f9fafb;
      border: 1px dotted #ccc;
      padding: 10px 14px;
      border-radius: 6px;
      white-space: pre-wrap;
    }
  </style>
</head>
<body>
  <h1>議事録</h1>

  <div class="info-box">
    <table>
      <tr>
        <td class="label">会議名：</td>
        <td>${fn:escapeXml(meeting.title)}</td>
      </tr>
      <tr>
        <td class="label">日付：</td>
        <td>${meeting.meetingDate}</td>
      </tr>
      <tr>
        <td class="label">時間：</td>
        <td>${meeting.startTime} ～ ${meeting.endTime}</td>
      </tr>
      <tr>
        <td class="label">参加者：</td>
        <td>${fn:escapeXml(meeting.participantsText)}</td>
      </tr>
    </table>
  </div>

  <c:forEach var="agenda" items="${meeting.agendas}">
    <div class="agenda-box">
      <div class="agenda-title">📄 議題：${fn:escapeXml(agenda.title)}</div>
      <div class="section-label">【発言内容】</div>
      <div class="section-content">${fn:escapeXml(agenda.speechNote)}</div>
      <div class="section-label">【決定事項】</div>
      <div class="section-content">${fn:escapeXml(agenda.decisionNote)}</div>
    </div>
  </c:forEach>

	<form method="post"
		action="${pageContext.request.contextPath}/download">
		<input type="hidden" name="meetingId" value="${meeting.meetingId}" />
		<input type="hidden" name="format" value="${param.format}" />
		<div style="text-align: center; margin-top: 40px;">
			<button type="submit"
				style="padding: 12px 24px; font-size: 1.1rem; background-color: #10b981; color: white; border: none; border-radius: 6px; cursor: pointer;">
				✅ この内容で出力する</button>
			<a href="${pageContext.request.contextPath}/download"
				style="margin-left: 20px; padding: 12px 24px; font-size: 1.1rem; background-color: #6b7280; color: white; text-decoration: none; border-radius: 6px; display: inline-block;">
				🔙 戻る </a>
		</div>
	</form>

</body>
</html>
