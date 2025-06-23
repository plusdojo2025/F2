document.addEventListener("DOMContentLoaded", function () {
  DownloadPage.init();
});

const DownloadPage = {
  init: function () {
    this.loadMeetingList();
    this.initFormSubmit();
  },

  // ✅ 会議一覧を取得してセレクトにセット
  loadMeetingList: async function () {
    try {
      const response = await fetch("/download/meetings"); // ← サーブレットに合わせて調整
      const meetings = await response.json();

      const select = document.querySelector("#meetingSelect");
      if (!select) return;

      // セレクトを初期化（※これがないと毎回追加されてしまう）
      select.innerHTML = '<option value="">-- 会議を選択してください --</option>';

      meetings.forEach(meeting => {
        const option = document.createElement("option");
        option.value = meeting.id;
        option.textContent = meeting.title;
        select.appendChild(option);
      });
    } catch (error) {
      console.error("会議一覧の取得に失敗しました:", error);
    }
  },

  // ✅ 出力時に会議が選択されているかチェック
  initFormSubmit: function () {
    const form = document.querySelector("#downloadForm");
    if (!form) return;

    form.addEventListener("submit", function (e) {
      const selected = document.querySelector("#meetingSelect").value;
      if (!selected) {
        e.preventDefault();
        alert("出力する会議を選択してください。");
      }
    });
  }
};
