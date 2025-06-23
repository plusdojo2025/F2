<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${meeting.title}|議事録プレビュー|ぎじろくん</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/preview.css">
</head>
<body>
	<header class="header">
		<h1 class="logo">📘 ぎじろくん</h1>
		<nav class="nav">
			<a href="${pageContext.request.contextPath}/dashboard">ホーム</a> 
			<a href="${pageContext.request.contextPath}/meeting/list">会議一覧</a> 
			<a href="${pageContext.request.contextPath}/meeting/create">会議作成</a> 
			<a href="${pageContext.request.contextPath}/download">出力</a> 
			<a href="${pageContext.request.contextPath}/settings">アカウント設定</a>
		</nav>
	</header>

	<main class="preview-wrapper">
		<div class="toggle-buttons">
			<button id="btn-detail" class="btn selected">詳細表示</button>
			<button id="btn-simple" class="btn">議題＋決定事項のみ</button>
		</div>

		<!-- 詳細表示 -->
		<section id="detail-view" class="preview-card">
			<h2 class="title">議事録</h2>

			<!-- 会議情報（中央寄せ + 会議名のみ下線） -->
			<div class="info">
				<p class="info-title">
					<strong>会議名：</strong> ${meeting.title}
				</p>
				<hr class="info-divider full" />

				<p>
					<strong>日付：</strong> ${meeting.meetingDate}
				</p>
				<p>
					<strong>時間：</strong> ${meeting.startTime} ～ ${meeting.endTime}
				</p>
				<hr class="info-divider full" />

				<p>
					<strong>参加者：</strong> ${meeting.participantsText}
				</p>
			</div>

			<!-- 議題リスト -->
			<div class="agenda-list">
				<c:forEach var="agenda" items="${agendas}">
					<div class="agenda-item">
						<div class="agenda-header">
							<h3 class="agenda-title">📋 議題：${agenda.title}</h3>
						</div>
						<hr class="divider">

						<div class="speech">
							<strong>【発言内容】</strong>
							<pre>${fn:escapeXml(agenda.speechNote)}</pre>
						</div>

						<div class="decision">
							<strong>【決定事項】</strong>
							<pre>${agenda.decisionNote}</pre>
						</div>
					</div>
				</c:forEach>
			</div>
		</section>

		<!-- 簡易表示 -->
		<section id="simple-view" class="preview-card hidden">
			<h3 class="title">議題・決定事項一覧</h3>
			<table class="simple-table">
				<thead>
					<tr>
						<th>議題</th>
						<th>決定事項</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="agenda" items="${agendas}">
						<tr>
							<td>${agenda.title}</td>
							<td>${agenda.decisionNote}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</section>

		<div class="action-buttons">
			<a href="${pageContext.request.contextPath}/meeting/list"
				class="btn cancel">戻る</a>
			<form action="${pageContext.request.contextPath}/meeting/finalize"
				method="post">
				<input type="hidden" name="fromPreview" value="true" />
				<input type="hidden" name="meetingId" value="${meeting.meetingId}" />
				<button type="submit" class="btn save">💾 保存</button>
			</form>
		</div>
	</main>

	<footer class="footer">&copy; 2025 えんじにっち. All rights
		reserved.</footer>

	<script>
    const btnDetail = document.getElementById("btn-detail");
    const btnSimple = document.getElementById("btn-simple");
    const detailView = document.getElementById("detail-view");
    const simpleView = document.getElementById("simple-view");

    btnDetail.onclick = () => {
      detailView.classList.remove("hidden");
      simpleView.classList.add("hidden");
      btnDetail.classList.add("selected");
      btnSimple.classList.remove("selected");
    };
    
    btnSimple.onclick = () => {
      simpleView.classList.remove("hidden");
      detailView.classList.add("hidden");
      btnSimple.classList.add("selected");
      btnDetail.classList.remove("selected");
    };
  </script>
</body>
</html>