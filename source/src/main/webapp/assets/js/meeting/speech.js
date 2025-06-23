document.addEventListener('DOMContentLoaded', () => {
  // HTML要素取得
  const addAgendaButton = document.getElementById('add-agenda-button');
  const agendaFormsContainer = document.getElementById('agenda-forms-container');
  const agendaTemplate = document.getElementById('agenda-form-template');
  const resetBtn = document.querySelector('button[type="reset"]');

  // モーダル取得
  const resetModal = document.getElementById('resetModal');
  const addModal = document.getElementById('addModal');

  // モーダル内のボタン
  const confirmResetBtn = document.getElementById('confirmReset');
  const cancelResetBtn = document.getElementById('cancelReset');
  const confirmAddBtn = document.getElementById('confirmAdd');
  const cancelAddBtn = document.getElementById('cancelAdd');

  let agendaIndex = document.querySelectorAll('.agenda-block').length;

  // ✅ 議題追加ロジックを関数化
  const addAgenda = () => {
    if (agendaTemplate && agendaFormsContainer) {
      const fragment = agendaTemplate.content.cloneNode(true);
      const div = fragment.firstElementChild;
      const replaced = div.outerHTML.replace(/PLACEHOLDER_INDEX/g, agendaIndex);
      agendaFormsContainer.insertAdjacentHTML('beforeend', replaced);
      agendaIndex++;
    }
  };

  // ✅ 議題追加ボタン → モーダル表示
  if (addAgendaButton) {
    addAgendaButton.addEventListener('click', (e) => {
      e.preventDefault();
      addModal.classList.remove('hidden');
    });
  }

  // モーダル：「はい」→追加処理
  if (confirmAddBtn) {
    confirmAddBtn.addEventListener('click', () => {
      addAgenda();
      addModal.classList.add('hidden');
    });
  }

  // モーダル：「キャンセル」
  if (cancelAddBtn) {
    cancelAddBtn.addEventListener('click', () => {
      addModal.classList.add('hidden');
    });
  }

  // リセットボタン → モーダル表示
  if (resetBtn) {
    resetBtn.addEventListener('click', (e) => {
      e.preventDefault();
      resetModal.classList.remove('hidden');
    });
  }

  // モーダル：「はい」→フォームリセット
  if (confirmResetBtn) {
    confirmResetBtn.addEventListener('click', () => {
      document.querySelector('form').reset();
      resetModal.classList.add('hidden');
    });
  }

  // モーダル：「キャンセル」
  if (cancelResetBtn) {
    cancelResetBtn.addEventListener('click', () => {
      resetModal.classList.add('hidden');
    });
  }

  // ✅ 動的に追加された議題の削除ボタンに対応
  if (agendaFormsContainer) {
    agendaFormsContainer.addEventListener('click', function(e) {
      if (e.target && e.target.classList.contains('delete-agenda-button')) {
        if (confirm('この議題を削除しますか？')) {
          const agendaBlock = e.target.closest('.agenda-block');
          if (agendaBlock) {
            agendaBlock.remove();
            // インデックスの再採番は、サーバー側で順序を担保するため、ここでは行わない
          }
        }
      }
    });
  }
});