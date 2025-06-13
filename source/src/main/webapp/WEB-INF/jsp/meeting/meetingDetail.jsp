<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>会議詳細</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/meetingDetail.css">
</head>
<body>
    <!-- ヘッダー -->
    <header class="header">
        <h1 class="main-title">🗒️ 議事録アプリ</h1>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/dashboard">ホーム</a>
            <a href="${pageContext.request.contextPath}/meeting/list">会議一覧</a>
            <a href="${pageContext.request.contextPath}/meeting/create">会議作成</a>
            <a href="${pageContext.request.contextPath}/download/preview">出力</a>
            <a href="${pageContext.request.contextPath}/settings">アカウント設定</a>
        </nav>
    </header>

    <div class="container">
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        <c:if test="${not empty meeting}">
            <div class="meeting-detail-card">
                <div class="meeting-title">${meeting.title}</div>
                <div class="meeting-meta">
                    <fmt:formatDate value="${meeting.meetingDate}" pattern="yyyy/MM/dd" />
                    &nbsp;
                    <fmt:formatDate value="${meeting.startTime}" pattern="HH:mm" />〜
                    <fmt:formatDate value="${meeting.endTime}" pattern="HH:mm" />
                </div>
                <div class="meeting-participants">
                    参加者：${meeting.participantsText}
                </div>
                <hr>
                <div class="meeting-detail-area">
                    <c:choose>
                        <c:when test="${fn:length(meeting.detailArea) > 200}">
                            <span id="detail-short">${fn:substring(meeting.detailArea, 0, 200)}...</span>
                            <span id="detail-full" style="display:none;">${meeting.detailArea}</span>
                            <button id="moreBtn" onclick="showFullDetail()">もっと見る</button>
                        </c:when>
                        <c:otherwise>
                            <span>${meeting.detailArea}</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${not empty meeting.decisions}">
                    <div class="meeting-decisions" style="margin-top: 1em;">
                        <b>決定事項</b><br>
                        <span>${meeting.decisions}</span>
                    </div>
                </c:if>
                <div class="meeting-actions">
                    <a href="${pageContext.request.contextPath}/meeting/edit?id=${meeting.meetingId}" class="btn btn-edit">編集</a>
                    <form action="${pageContext.request.contextPath}/meeting/delete" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${meeting.meetingId}">
                        <button type="submit" class="btn btn-delete" onclick="return confirm('本当に削除しますか？');">削除</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/meeting/list" class="btn btn-cancel">閉じる</a>
                </div>
            </div>
        </c:if>
    </div>
    <script>
    function showFullDetail() {
        document.getElementById('detail-short').style.display = 'none';
        document.getElementById('detail-full').style.display = 'inline';
        document.getElementById('moreBtn').style.display = 'none';
    }
    </script>
</body>
</html>