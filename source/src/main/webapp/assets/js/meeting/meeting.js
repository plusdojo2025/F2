// assets/js/meeting/meeting.js

// 会議一覧ページの機能
const MeetingList = {
    init: function() {
        this.initSearchForm();
        this.initSortSelect();
        this.initDeleteModal(); // 削除モーダルの初期化を追加
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

// 会議詳細ページの機能
const MeetingDetail = {
    init: function() {
        this.initMoreButton();
    },

    // 「もっと見る」ボタンの初期化
    initMoreButton: function() {
        const moreBtn = document.getElementById('moreBtn');
        if (!moreBtn) return;

        moreBtn.addEventListener('click', function() {
            document.getElementById('detail-short').style.display = 'none';
            document.getElementById('detail-full').style.display = 'inline';
            this.style.display = 'none';
        });
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
    // MeetingDelete.init(); ← これは不要
});