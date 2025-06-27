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
        const editLink = document.getElementById('editLink');

        // ローディング表示
        modalTitle.textContent = '読み込み中...';
        agendaBlock.innerHTML = '<div class="loading">読み込み中...</div>';
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
                        let html = `<div class="agenda-title" style="font-weight:bold;color:#1ca97c;font-size:1.1em;margin:16px 0 8px 0;">[${agenda.title}]</div>`;
                        if (agenda.speechNote) {
                            // 発言を「名前：内容」で複数行に分割
                            const speeches = agenda.speechNote.split('\n');
                            html += '<div class="speech-list">';
                            speeches.forEach((line, idx) => {
                                if (idx === 0) {
                                    html += `<div class="speech-line" style="color:#1ca97c;margin-left:8px;margin-bottom:2px;">${line}</div>`;
                                } else {
                                    html += `<div class="speech-line more-speech" style="color:#1ca97c;margin-left:8px;margin-bottom:2px;display:none;">${line}</div>`;
                                }
                            });
                            if (speeches.length > 1) {
                                html += `<button class="show-more-btn" style="color:#1ca97c;background:none;border:none;cursor:pointer;margin-top:4px;">▼もっと見る</button>`;
                            }
                            html += '</div>';
                        }
                        // 議題の決定事項も表示
                        if (agenda.decisionNote) {
                            const decisionHtml = agenda.decisionNote.replace(/\n/g, '<br>');
                            html += `<div class="agenda-decision">
                                       <strong>決定事項</strong>
                                       <p class="decision-text">${decisionHtml}</p>
                                     </div>`;
                        }
                        agendaBlock.innerHTML += html;
                    });
                    // もっと見るボタンの動作
                    agendaBlock.querySelectorAll('.show-more-btn').forEach(btn => {
                        btn.onclick = function() {
                            this.parentNode.querySelectorAll('.more-speech').forEach(e => e.style.display = 'block');
                            this.style.display = 'none';
                            
                            // ボタンを押した後、モーダルのスクロール位置を調整
                            setTimeout(() => {
                                const modalBody = document.querySelector('#detailModal .modal-body');
                                if (modalBody) {
                                    // 新しく表示された内容の位置までスクロール
                                    const newContent = this.parentNode.querySelectorAll('.more-speech');
                                    if (newContent.length > 0) {
                                        const lastNewContent = newContent[newContent.length - 1];
                                        const rect = lastNewContent.getBoundingClientRect();
                                        const modalRect = modalBody.getBoundingClientRect();
                                        
                                        if (rect.bottom > modalRect.bottom) {
                                            modalBody.scrollTop += (rect.bottom - modalRect.bottom) + 20;
                                        }
                                    }
                                }
                            }, 100);
                        };
                    });
                } else if (data.detailArea) {
                    agendaBlock.innerHTML = data.detailArea;
                } else {
                    agendaBlock.innerHTML = '議題・発言はありません。';
                }

                editLink.href = `/${contextPath}/meeting/edit?id=${data.meetingId}`;
            })
            .catch(error => {
                console.error('Error:', error);
                modalTitle.textContent = 'エラー';
                agendaBlock.innerHTML = '<div class="error-message">データの取得に失敗しました。</div>';
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
                                <td style="word-break:break-all;">${agenda.decisionNote || '決定事項なし'}</td>
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
    init: function () {
        this.initTabSwitch();
        this.initButtons();
    },

    initTabSwitch: function () {
    const tabs = ['meeting-tab', 'speech-tab'];
    const buttons = document.querySelectorAll('.tab-buttons button');

    buttons.forEach((button, index) => {
        button.addEventListener('click', function () {
            // ボタンのテキストでタブを判定
            const target = this.textContent.includes('会議') ? 'meeting-tab' : 'speech-tab';

            // 全タブを非表示にする
            tabs.forEach(tabId => {
                const tab = document.getElementById(tabId);
                if (tab) {
                    tab.classList.add('hidden');
                }
            });

            // 対象のタブを表示する
            const targetTab = document.getElementById(target);
            if (targetTab) {
                targetTab.classList.remove('hidden');
            }
            
            // ボタンのアクティブ状態を更新
            buttons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
        });
    });
    
    // 初期状態で会議タブをアクティブにする
    if (buttons.length > 0) {
        buttons[0].classList.add('active');
    }
},

    // ボタン機能（追加／削除／保存／プレビュー）
    initButtons: function () {
        const contextPath = '/' + window.location.pathname.split('/')[1];

        // 議題を追加
        const addBtn = document.getElementById('add-agenda-button');
        if (addBtn) {
            addBtn.addEventListener('click', function () {
                const agendaContainer = document.getElementById('agenda-container');
                const template = document.getElementById('agenda-template');
                if (template && agendaContainer) {
                    const clone = template.content.cloneNode(true);
                    
                    // インデックスを正しく設定
                    const agendaBlocks = agendaContainer.querySelectorAll('.agenda-block');
                    const newIndex = agendaBlocks.length;
                    
                    // name属性のINDEXを実際のインデックスに置換
                    clone.querySelectorAll('[name*="INDEX"]').forEach(element => {
                        element.name = element.name.replace('INDEX', newIndex);
                    });
                    
                    agendaContainer.appendChild(clone);
                }
            });
        }

        // 削除（動的対応）
        document.addEventListener('click', function (e) {
            if (e.target && e.target.classList.contains('delete-agenda-button')) {
                if (confirm('この議題を削除しますか？')) {
                    const agendaBlock = e.target.closest('.agenda-block');
                    if (agendaBlock) {
                        agendaBlock.remove();
                        // 削除後にインデックスを再採番
                        reindexAgendas();
                    }
                }
            }
        });

        function reindexAgendas() {
            const agendaContainer = document.getElementById('agenda-container');
            if (!agendaContainer) return;

            const agendaBlocks = agendaContainer.querySelectorAll('.agenda-block');
            agendaBlocks.forEach((block, index) => {
                // name属性を更新: agendas[5] -> agendas[2]
                block.querySelectorAll('[name*="agendas["]').forEach(input => {
                    input.name = input.name.replace(/agendas\[\d+\]/, `agendas[${index}]`);
                });
            });
        }

        // data-confirm属性の処理
        document.addEventListener('click', function (e) {
            if (e.target && e.target.hasAttribute('data-confirm')) {
                const confirmMessage = e.target.getAttribute('data-confirm');
                if (!confirm(confirmMessage)) {
                    e.preventDefault();
                    return false;
                }
            }
        });

        // 変更を保存（submitボタン）
        const submitBtn = document.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.addEventListener('click', function (e) {
                // 編集モードの場合のみ確認ダイアログを表示
                const isEditMode = document.querySelector('input[name="meetingId"]').value !== '';
                if (isEditMode) {
                    const speechTab = document.getElementById('speech-tab');
                    const isSpeechTabVisible = speechTab && !speechTab.classList.contains('hidden');
                    
                    if (isSpeechTabVisible) {
                        // 発言タブが表示されている場合：直接保存して一覧に戻る
                        // 何もしない（通常のフォーム送信）
                    } else {
                        // 会議タブが表示されている場合：確認ダイアログを表示
                        e.preventDefault();
                        const choice = confirm('発言も編集しますか？\n\nOK → 発言編集画面に移動\nCancel → 会議情報のみ保存して一覧に戻る');
                        
                        if (choice) {
                            // OK：発言タブに移動
                            const meetingTab = document.getElementById('meeting-tab');
                            const tabButtons = document.querySelectorAll('.tab-buttons button');
                            
                            if (speechTab && meetingTab) {
                                meetingTab.classList.add('hidden');
                                speechTab.classList.remove('hidden');
                                
                                // タブボタンのアクティブ状態を更新
                                tabButtons.forEach(btn => btn.classList.remove('active'));
                                tabButtons[1].classList.add('active'); // 発言タブをアクティブに
                                
                                // 保存ボタンのテキストを変更
                                submitBtn.textContent = '発言を保存して一覧に戻る';
                            }
                        } else {
                            // Cancel：フォームを送信して一覧に戻る
                            const form = submitBtn.closest('form');
                            if (form) {
                                form.submit();
                            }
                        }
                    }
                }
                // 作成モードの場合は通常通り送信
            });
        }

        // プレビューで確認
        const previewBtn = document.querySelector('button[formaction*="preview"]');
        if (previewBtn) {
            previewBtn.addEventListener('click', function (e) {
                // data-confirm属性の処理は上記で既に処理される
                // formaction属性により自動的にプレビューページに送信される
            });
        }
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