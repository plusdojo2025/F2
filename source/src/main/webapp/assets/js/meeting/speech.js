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
      // 新しいインデックスでプレースホルダーを置換
      const newIndex = agendaFormsContainer.querySelectorAll('.agenda-block').length;
      const replaced = div.outerHTML.replace(/PLACEHOLDER_INDEX/g, newIndex);
      agendaFormsContainer.insertAdjacentHTML('beforeend', replaced);
      agendaIndex = newIndex + 1;
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
            reindexAgendas(); // 削除後にインデックスを再採番
          }
        }
      }
    });
  }
  
  function reindexAgendas() {
      const remainingBlocks = agendaFormsContainer.querySelectorAll('.agenda-block');
      remainingBlocks.forEach((block, index) => {
        // name属性を更新: agendas[5] -> agendas[2]
        block.querySelectorAll('[name*="agendas["]').forEach(input => {
          input.name = input.name.replace(/agendas\[\d+\]/, `agendas[${index}]`);
        });
        // id属性を更新: agenda5-title -> agenda2-title
        block.querySelectorAll('[id*="agenda"]').forEach(element => {
            if (element.id) {
                 element.id = element.id.replace(/agenda\d+/, `agenda${index}`);
            }
        });
        // for属性を更新
        block.querySelectorAll('label[for*="agenda"]').forEach(label => {
            if(label.htmlFor){
                label.htmlFor = label.htmlFor.replace(/agenda\d+/, `agenda${index}`);
            }
        });
      });
      // 次に追加する議題のためのインデックスも更新
      agendaIndex = remainingBlocks.length;
  }
});