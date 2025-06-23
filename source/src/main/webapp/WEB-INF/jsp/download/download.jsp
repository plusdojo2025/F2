<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>議事録の出力 | ぎじろくん</title>

<!-- CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/download.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
	<!-- 共通ヘッダー -->
	<header class="header">
		<h1 class="logo">📝 ぎじろくん</h1>
		<nav class="nav">
			<a href="${pageContext.request.contextPath}/dashboard">ホーム</a> <a
				href="${pageContext.request.contextPath}/meeting/list">会議一覧</a> <a
				href="${pageContext.request.contextPath}/meeting/create">会議作成</a> <a
				href="${pageContext.request.contextPath}/download">出力</a> <a
				href="${pageContext.request.contextPath}/settings">アカウント設定</a>
		</nav>
	</header>

	<!-- タイトル -->
	<h2 class="page-title">🖨️ 議事録のダウンロード</h2>

	<!-- 出力フォーム -->
	<main>
		<form class="download-form" onsubmit="goToPreview(); return false;">
			<div class="form-group-row">
				<!-- 会議名検索 -->
				<div class="form-box">
					<label for="search-name">会議名検索</label> 
					<div class="search-input-group">
						<input type="text"
							id="search-name" name="searchName" placeholder="会議名を入力" value="${searchName}" />
						<button type="button" onclick="searchMeetings()" class="search-btn">🔍</button>
					</div>
				</div>

				<!-- 日付検索 -->
				<div class="form-box">
					<label for="search-date">日付検索</label> 
					<div class="search-input-group">
						<input type="date"
							id="search-date" name="searchDate" value="${searchDate}" />
						<button type="button" onclick="searchMeetings()" class="search-btn">🔍</button>
					</div>
				</div>

				<!-- 会議選択 -->
				<div class="form-box">
					<label for="meeting-select">出力する会議を選択</label> <select
						id="meeting-select" name="meetingId" required>
						<option value="">-- 会議を選択してください --</option>
						<c:forEach var="m" items="${meetingList}">
							<option value="${m.meeting_id}">${m.title}</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<!-- 出力形式 -->
			<div class="form-group">
				<label>出力形式</label>
				<div class="radio-group">
					<label class="radio-label"> <input type="radio"
						name="format" value="pdf" checked /> <span>PDF</span>
					</label> <label class="radio-label"> <input type="radio"
						name="format" value="text" /> <span>Text</span>
					</label>
				</div>
			</div>

			<!-- 出力ボタン -->
			<div class="form-actions">
				<button type="submit">出力する</button>
			</div>
		</form>
	</main>

	<!-- フッター -->
	<footer> &copy; 2025 えんじにっち. All rights reserved. </footer>

	<!-- プレビュージャンプスクリプト -->
	<script>
	  const contextPath = "${pageContext.request.contextPath}";
	
	  function goToPreview() {
	    const meetingId = document.getElementById("meeting-select").value;
	    const format = document.querySelector('input[name="format"]:checked')?.value;
	    const searchName = document.getElementById("search-name").value;
	    const searchDate = document.getElementById("search-date").value;
	
	    if (!meetingId) {
	      alert("会議を選択してください。");
	      return;
	    }
	
	    const params = new URLSearchParams();
	    params.append("meetingId", meetingId);
	    params.append("format", format);
	    if (searchName) params.append("searchName", searchName);
	    if (searchDate) params.append("searchDate", searchDate);
	
	    window.location.href = contextPath + "/preview?" + params.toString();
	  }

	  // 会議名検索機能
	  function searchMeetings() {
	    const searchName = document.getElementById("search-name").value;
	    const searchDate = document.getElementById("search-date").value;
	    
	    console.log("検索実行:", { searchName, searchDate }); // デバッグログ
	    
	    const params = new URLSearchParams();
	    if (searchName) params.append("searchName", searchName);
	    if (searchDate) params.append("searchDate", searchDate);
	
	    console.log("検索URL:", contextPath + "/download?" + params.toString()); // デバッグログ
	
	    // 検索リクエストを送信
	    fetch(contextPath + "/download?" + params.toString())
	      .then(response => {
	        if (response.ok) {
	          // ページをリロードして検索結果を表示
	          window.location.href = contextPath + "/download?" + params.toString();
	        } else {
	          alert("検索中にエラーが発生しました。");
	        }
	      })
	      .catch(error => {
	        console.error("検索エラー:", error);
	        alert("検索中にエラーが発生しました。");
	      });
	  }

	  // Enterキーで検索実行
	  document.getElementById("search-name").addEventListener("keypress", function(event) {
	    if (event.key === "Enter") {
	      event.preventDefault();
	      searchMeetings();
	    }
	  });

	  // 日付変更時に自動検索
	  document.getElementById("search-date").addEventListener("change", function() {
	    searchMeetings();
	  });
	</script>

</body>
</html>
