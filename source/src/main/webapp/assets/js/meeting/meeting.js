// assets/js/meeting/meeting.js

// 会議一覧ページの機能
const MeetingList = {
    init: function() {
        this.initSearchForm();
        this.initSortSelect();
        this.initDeleteModal();
    },

    // 検索フォームの初期化
    initSearchForm: function() {
        const searchForm = document.querySelector('.search-area');
        if (!searchForm) return;

        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const meetingName = document.querySelector('input[name="meetingName"]').value;
            const meetingDate = document.querySelector('input[name="meetingDate"]').value;
            const sort = document.querySelector('select[name="sort"]').value;
            
            // URLパラメータの構築
            const params = new URLSearchParams();
            if (meetingName) params.append('meetingName', meetingName);
            if (meetingDate) params.append('meetingDate', meetingDate);
            if (sort) params.append('sort', sort);
            
            // ページ遷移
            window.location.href = `${searchForm.action}?${params.toString()}`;
        });
    },

    // ソート選択の初期化
    initSortSelect: function() {
        const sortSelect = document.querySelector('select[name="sort"]');
        if (!sortSelect) return;

        sortSelect.addEventListener('change', function() {
            this.form.submit();
        });
    },
    
    // 詳細を表示する関数
    showDetail: function(meetingId) {
        console.log('Showing detail for meeting ID:', meetingId);

        // モーダルの要素を取得
        const modal = document.getElementById('detailModal');
        const modalTitle = document.getElementById('detailTitle');
        const detailDate = document.getElementById('detailDate');
        const detailTime = document.getElementById('detailTime');
        const detailParticipants = document.getElementById('detailParticipants');
        const agendaBlock = document.getElementById('detailAgendaBlock');
        const detailDecisions = document.getElementById('detailDecisions');
        const editLink = document.getElementById('editLink');

        // ローディング表示
        modalTitle.textContent = '読み込み中...';
        agendaBlock.innerHTML = '<div class="loading">読み込み中...</div>';
        detailDecisions.innerHTML = '';
        modal.classList.remove('hidden');
        modal.classList.add('show');

        // 会議詳細データの取得
        const contextPath = window.location.pathname.split('/')[1];
        fetch(`/${contextPath}/meeting/detail?id=${meetingId}&format=json`)
            .then(response => {
                console.log('Response status:', response.status);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Received data:', data);
                if (data.error) {
                    modalTitle.textContent = 'エラー';
                    agendaBlock.innerHTML = `<div class="error-message">${data.error}</div>`;
                    detailDecisions.innerHTML = '';
                    return;
                }

                // モーダルの内容を更新
                modalTitle.textContent = data.title;
                detailDate.innerHTML = `<i class="far fa-calendar"></i> ${this.formatDate(data.meetingDate)}`;
                detailTime.innerHTML = `<i class="far fa-clock"></i> ${this.formatTime(data.startTime)}〜${this.formatTime(data.endTime)}`;
                detailParticipants.textContent = data.participantsText;

                // DBデータで議題・発言を描画
                agendaBlock.innerHTML = '';
                if (data.agendas && data.agendas.length > 0) {
                    data.agendas.forEach(agenda => {
                        let html = `<div class=\"agenda-title\" style=\"font-weight:bold;color:#1ca97c;font-size:1.1em;margin:16px 0 8px 0;\">[${agenda.title}]</div>`;
                        if (agenda.speechNote) {
                            // 発言を「名前：内容」で複数行に分割
                            const speeches = agenda.speechNote.split('\n');
                            html += '<div class="speech-list">';
                            speeches.forEach((line, idx) => {
                                if (idx === 0) {
                                    html += `<div class=\"speech-line\" style=\"color:#1ca97c;margin-left:8px;margin-bottom:2px;\">${line}</div>`;
                                } else {
                                    html += `<div class=\"speech-line more-speech\" style=\"color:#1ca97c;margin-left:8px;margin-bottom:2px;display:none;\">${line}</div>`;
                                }
                            });
                            if (speeches.length > 1) {
                                html += `<button class=\"show-more-btn\" style=\"color:#1ca97c;background:none;border:none;cursor:pointer;margin-top:4px;\">▼もっと見る</button>`;
                            }
                            html += '</div>';
                        }
                        agendaBlock.innerHTML += html;
                    });
                    // もっと見るボタンの動作
                    agendaBlock.querySelectorAll('.show-more-btn').forEach(btn => {
                        btn.onclick = function() {
                            this.parentNode.querySelectorAll('.more-speech').forEach(e => e.style.display = 'block');
                            this.style.display = 'none';
                        };
                    });
                } else if (data.detailArea) {
                    agendaBlock.innerHTML = data.detailArea;
                } else {
                    agendaBlock.innerHTML = '議題・発言はありません。';
                }

                // 決定事項
                detailDecisions.innerHTML = data.decisions || '決定事項はありません。';
                editLink.href = `/${contextPath}/meeting/edit?id=${data.meetingId}`;
            })
            .catch(error => {
                console.error('Error:', error);
                modalTitle.textContent = 'エラー';
                agendaBlock.innerHTML = '<div class="error-message">データの取得に失敗しました。</div>';
                detailDecisions.innerHTML = '';
            });
    },

    // 簡易詳細を表示する関数
    showQuickDetail: function(meetingId) {
        console.log('Showing quick detail for meeting ID:', meetingId);

        // モーダルの要素を取得
        const modal = document.getElementById('quickDetailModal');
        const modalTitle = document.getElementById('quickDetailTitle');
        const quickDetailDate = document.getElementById('quickDetailDate');
        const quickDetailTime = document.getElementById('quickDetailTime');
        const quickDetailParticipants = document.getElementById('quickDetailParticipants');
        const quickDetailDecisions = document.getElementById('quickDetailDecisions');

        // ローディング表示
        modalTitle.textContent = '読み込み中...';
        modal.classList.remove('hidden');
        modal.classList.add('show');

        // 会議詳細データの取得
        const contextPath = window.location.pathname.split('/')[1];
        fetch(`/${contextPath}/meeting/detail?id=${meetingId}&format=json`)
            .then(response => {
                console.log('Response status:', response.status);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Received data:', data);
                if (data.error) {
                    modalTitle.textContent = 'エラー';
                    return;
                }

                // モーダルの内容を更新
                modalTitle.textContent = data.title;
                quickDetailDate.innerHTML = `<i class="far fa-calendar"></i> ${this.formatDate(data.meetingDate)}`;
                quickDetailTime.innerHTML = `<i class="far fa-clock"></i> ${this.formatTime(data.startTime)}〜${this.formatTime(data.endTime)}`;
                quickDetailParticipants.textContent = `参加者：${data.participantsText}`;

                // tableBody取得と描画をモーダル表示後に遅延して実行
                setTimeout(() => {
                    const tableBody = document.querySelector('#quickAgendaDecisionTable tbody');
                    if (!tableBody) {
                        console.warn('quickAgendaDecisionTable tbody が見つかりません');
                        return;
                    }
                    tableBody.innerHTML = '';
                    if (data.agendas && data.agendas.length > 0) {
                        data.agendas.forEach(agenda => {
                            tableBody.innerHTML += `<tr>
                                <td style="word-break:break-all;">${agenda.title}</td>
                                <td style="word-break:break-all;">${agenda.decisionNote || ''}</td>
                            </tr>`;
                        });
                    } else {
                        tableBody.innerHTML = '<tr><td colspan="2">議題・決定事項はありません。</td></tr>';
                    }
                }, 100);
            })
            .catch(error => {
                console.error('Error:', error);
                modalTitle.textContent = 'エラー';
            });
    },

    // 日付をフォーマットする関数
    formatDate: function(dateStr) {
        const date = new Date(dateStr);
        return date.toLocaleDateString('ja-JP', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    },

    // 時間をフォーマットする関数
    formatTime: function(timeStr) {
        const [hours, minutes] = timeStr.split(':');
        return `${hours}:${minutes}`;
    },

    // 削除モーダルの初期化
    initDeleteModal: function() {
        window.showModal = function(modalId, meetingId) {
            const modal = document.getElementById(modalId);
            document.getElementById('deleteMeetingId').value = meetingId;
            modal.classList.remove('hidden');
            modal.classList.add('show');
        };

        window.closeModal = function(modalId) {
            const modal = document.getElementById(modalId);
            modal.classList.remove('show');
            modal.classList.add('hidden');
        };

        window.onclick = function(event) {
            if (event.target.classList.contains('modal')) {
                event.target.classList.remove('show');
                event.target.classList.add('hidden');
            }
        };
    }
};

// 会議編集ページの機能
const MeetingEdit = {
    init: function() {
        // 編集ページの初期化処理
    }
};

// ページ読み込み時の初期化
document.addEventListener('DOMContentLoaded', function() {
    if (document.querySelector('.meeting-list')) {
        MeetingList.init();
    }
    if (document.querySelector('.meeting-detail-card')) {
        MeetingDetail.init();
    }
    if (document.querySelector('.meeting-edit')) {
        MeetingEdit.init();
    }
});