<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<title>会議作成・編集 | ぎじろくん</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/meetingForm.css" />
<script
	src="${pageContext.request.contextPath}/assets/js/meeting/meeting.js"
	defer></script>
</head>
<body>
	<!-- 共通ヘッダー -->
	<header class="header">
		<h1 class="logo">📘 ぎじろくん</h1>
		<nav class="nav">
			<a href="${pageContext.request.contextPath}/dashboard">ホーム</a> <a
				href="${pageContext.request.contextPath}/meeting/list">会議一覧</a> <a
				href="${pageContext.request.contextPath}/meeting/create">会議作成</a> <a
				href="${pageContext.request.contextPath}/download">出力</a> <a
				href="${pageContext.request.contextPath}/settings">アカウント設定</a>
		</nav>
	</header>

	<!-- ページタイトル -->
	<h2 class="page-title">
		<c:choose>
			<c:when test="${not empty meeting}">🛠 会議の変更</c:when>
			<c:otherwise>✏️ 会議作成</c:otherwise>
		</c:choose>
	</h2>

	<!-- エラーメッセージ表示 -->
	<c:if test="${not empty error}">
		<div class="error-message">
			${error}
		</div>
	</c:if>

	<!-- タブ切替（編集モード時のみ） -->
	<c:if test="${not empty meeting}">
		<div class="tab-buttons">
			<button type="button">会議</button>
			<button type="button">発言</button>
		</div>
	</c:if>

	<main class="main-container meeting-edit">
		<!-- 統合フォーム -->
		<form method="post"
			action="${pageContext.request.contextPath}/meeting/${not empty meeting ? 'edit' : 'create'}">
			<input type="hidden" name="meetingId" value="${meeting.meetingId}" />
			<input type="hidden" name="csrfToken" value="${csrfToken}" />

			<!-- 会議フォーム -->
			<section id="meeting-tab" class="tab-content">
				<div class="form-group">
					<label for="title">会議名</label> <input type="text" id="title"
						name="title" value="${meeting.title}" required />
				</div>

				<div class="form-row">
					<div class="form-group">
						<label for="date">日付</label> <input type="date" id="date"
							name="date" value="${meeting.meetingDate}" required />
					</div>

					<c:choose>
						<c:when test="${not empty meeting}">
							<div class="form-group">
								<label for="startTime">開始時間</label> <input type="time"
									id="startTime" name="startTime" value="${meeting.startTime}"
									required />
							</div>
							<div class="form-group">
								<label for="endTime">終了時間</label> <input type="time"
									id="endTime" name="endTime" value="${meeting.endTime}" required />
							</div>
						</c:when>

						<c:otherwise>
							<div class="form-group">
								<label for="time">時間</label> <input type="text" id="time"
									name="time" placeholder="例：10:00~11:00" required />
							</div>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="form-group">
					<label for="participants">参加者（カンマ区切り）</label> <input type="text"
						id="participants" name="participants"
						value="${meeting.participantsText}" required />
				</div>
			</section>

			<!-- 議事録編集 -->
			<section id="speech-tab" class="tab-content hidden">
				<div id="agenda-container">

					<c:choose>
						<c:when test="${not empty agendas and fn:length(agendas) > 0}">
							<c:forEach var="agenda" items="${agendas}" varStatus="i">
								<div class="agenda-block">
								<input type="hidden" name="agendas[${i.index}].agendaId" value="${agenda.agendaId}" />
									<label>議題</label> <input type="text"
										name="agendas[${i.index}].title" value="${agenda.title}"
										required /> <label>発言内容</label>
									<textarea name="agendas[${i.index}].speechNote" required>${agenda.speechNote}</textarea>

									<label>決定事項</label>
									<textarea name="agendas[${i.index}].decisionNote">${agenda.decisionNote}</textarea>

									<button type="button" class="delete-agenda-button">🗑️ 削除</button>
								</div>
							</c:forEach>
						</c:when>

						<c:otherwise>
							<!-- 会議作成時は空の議題ブロックを表示しない -->
						</c:otherwise>
					</c:choose>


				</div>

				<div class="button-row">
					<button type="button" id="add-agenda-button" class="button next">＋
						議題を追加</button>
				</div>
			</section>

			<!-- 統合ボタン -->
			<div class="button-row">
				<c:choose>
					<c:when test="${not empty meeting}">
						<button type="submit" class="button submit"
							data-confirm="この内容で保存しますか？">変更を保存</button>
						<a href="${pageContext.request.contextPath}/meeting/list"
							class="button cancel" data-confirm="変更を破棄して戻りますか？">キャンセル</a>
						<button type="submit"
							formaction="${pageContext.request.contextPath}/meeting/preview"
							class="button preview" data-confirm="保存後にプレビューへ移動しますか？">プレビューで確認</button>
					</c:when>
					<c:otherwise>
						<button type="submit" name="action" value="create"
							class="button submit">会議だけ作成</button>
						<button type="submit" name="action" value="next"
							class="button next">次へ（発言入力）</button>
					</c:otherwise>
				</c:choose>
			</div>
		</form>

		<!-- 議題追加用テンプレート -->
		<template id="agenda-template">
			<div class="agenda-block">
				<input type="hidden" name="agendas[INDEX].agendaId" value="" />
				<label>議題</label>
				<input type="text" name="agendas[INDEX].title" placeholder="議題タイトル" />
				<label>発言内容</label>
				<textarea name="agendas[INDEX].speechNote" placeholder="発言内容"></textarea>
				<label>決定事項</label>
				<textarea name="agendas[INDEX].decisionNote" placeholder="決定事項"></textarea>
				<button type="button" class="delete-agenda-button">🗑️ 削除</button>
			</div>
		</template>
	</main>

	<footer class="footer"> &copy; 2025 えんじにっち. All rights
		reserved. </footer>
</body>
</html>
