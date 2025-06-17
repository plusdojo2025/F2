document.addEventListener("DOMContentLoaded", () => {
  const searchNameInput = document.getElementById("search-name");
  const searchDateInput = document.getElementById("search-date");
  const meetingSelect = document.getElementById("meeting-select");

  async function fetchMeetings() {
    const name = searchNameInput.value;
    const date = searchDateInput.value;

    const params = new URLSearchParams();
    if (name) params.append("name", name);
    if (date) params.append("date", date);

    try {
      const response = await fetch(`/your-app-context/download/searchMeetings?${params.toString()}`);
      if (!response.ok) throw new Error("通信エラー");

      const meetings = await response.json();

      meetingSelect.innerHTML = '<option value="">-- 会議を選択してください --</option>';

      meetings.forEach(meeting => {
        const option = document.createElement("option");
        option.value = meeting.meetingId;
        option.textContent = `${meeting.title}（${meeting.meetingDate}）`;
        meetingSelect.appendChild(option);
      });
    } catch (error) {
      console.error("検索に失敗しました", error);
    }
  }

  searchNameInput.addEventListener("input", fetchMeetings);
  searchDateInput.addEventListener("input", fetchMeetings);

  fetchMeetings();
});
