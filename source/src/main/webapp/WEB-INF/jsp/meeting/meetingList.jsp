<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>議事録支援アプリ | 会議一覧</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/meetingList.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <!-- 共通ヘッダー -->
    <header class="header">
        <h1 class="main-title">🗒️ ぎじろくん</h1>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/dashboard">ホーム</a>
            <a href="${pageContext.request.contextPath}/meeting/list">会議一覧</a>
            <a href="${pageContext.request.contextPath}/meeting/create">会議作成</a>
            <a href="${pageContext.request.contextPath}/download/preview">出力</a>
            <a href="${pageContext.request.contextPath}/settings">アカウント設定</a>
        </nav>
    </header>

    <div class="container">
        <h1 class="page-title">会議一覧</h1>
        <form class="search-area" method="get" action="${pageContext.request.contextPath}/meeting/list">
            <input type="text" name="meetingName" placeholder="会議名を入力" class="input" value="${fn:escapeXml(meetingName)}">
            <input type="date" name="meetingDate" placeholder="年/月/日" class="input" value="${fn:escapeXml(meetingDate)}">
            <select name="sort" class="input">
                <option value="dateDesc" ${sort == 'dateDesc' ? 'selected' : ''}>開催日(新しい順)</option>
                <option value="dateAsc" ${sort == 'dateAsc' ? 'selected' : ''}>開催日(古い順)</option>
                <option value="nameAsc" ${sort == 'nameAsc' ? 'selected' : ''}>会議名(昇順)</option>
                <option value="nameDesc" ${sort == 'nameDesc' ? 'selected' : ''}>会議名(降順)</option>
            </select>
            <button type="submit" class="btn btn-search">検索</button>
        </form>
        <div class="meeting-list">
            <c:forEach var="meeting" items="${meetings}">
                <div class="meeting-card">
                    <div class="meeting-header">
                        <h3 class="meeting-title">${meeting.title}</h3>
                        <div class="meeting-date">
                            <i class="far fa-calendar"></i>
                            <fmt:formatDate value="${meeting.meetingDate}" pattern="yyyy年MM月dd日"/>
                        </div>
                    </div>
                    <div class="meeting-info">
                        <div class="meeting-time">
                            <i class="far fa-clock"></i>
                            <fmt:formatDate value="${meeting.startTime}" pattern="HH:mm"/>〜
                            <fmt:formatDate value="${meeting.endTime}" pattern="HH:mm"/>
                        </div>
                        <div class="meeting-participants">
                            <i class="fas fa-users"></i>
                            参加者：${meeting.participantsText}
                        </div>
                    </div>
                    <div class="meeting-actions">
                        <a href="${pageContext.request.contextPath}/meeting/detail?id=${meeting.meetingId}" class="btn btn-detail">詳細</a>
                        <a href="${pageContext.request.contextPath}/meeting/quickDetail?id=${meeting.meetingId}" class="btn btn-quick-detail">簡易詳細</a>
                        <a href="${pageContext.request.contextPath}/meeting/edit?id=${meeting.meetingId}" class="btn btn-edit">編集</a>
                        <form action="${pageContext.request.contextPath}/meeting/delete" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="${meeting.meetingId}">
                            <button type="button" class="btn btn-delete" onclick="showModal('deleteModal', ${meeting.meetingId})">削除</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
        <!-- 削除モーダル -->
        <div id="deleteModal" class="modal hidden">
            <div class="modal-content">
                <h3>会議の削除</h3>
                <p>この会議を削除してもよろしいですか？</p>
                <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/meeting/delete">
                    <input type="hidden" name="id" id="deleteMeetingId">
                    <div class="modal-buttons">
                        <button type="button" class="btn btn-cancel" onclick="closeModal('deleteModal')">キャンセル</button>
                        <button type="submit" class="btn btn-confirm">削除する</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="pagination">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="?page=${i}&meetingName=${fn:escapeXml(meetingName)}&meetingDate=${fn:escapeXml(meetingDate)}&sort=${fn:escapeXml(sort)}"
                   class="page-btn${i == currentPage ? ' active' : ''}">${i}</a>
            </c:forEach>
        </div>
    </div>
    <!-- JSはbodyの最後で読み込むのが確実 -->
    <script src="${pageContext.request.contextPath}/assets/js/meeting/meeting.js"></script>
</body>
</html>