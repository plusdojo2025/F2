document.addEventListener("DOMContentLoaded", () => {
  const calendarEl = document.getElementById("calendar");
  if (!calendarEl) return;

  let current = new Date();
  current.setDate(1);

  function renderCalendar(date) {
    const year = date.getFullYear();
    const month = date.getMonth();
    const today = new Date();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const firstDay = new Date(year, month, 1).getDay();
    const monthNames = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];
    const dayNames = ["日", "月", "火", "水", "木", "金", "土"];

    calendarEl.classList.add("fade-in");
    setTimeout(() => calendarEl.classList.remove("fade-in"), 400);

    let html = `
      <div class="calendar-header">
        <button id="prevMonth" aria-label="前月">◀</button>
        <h3><span class="calendar-icon">📆</span> ${year}年 ${monthNames[month]}</h3>
        <button id="nextMonth" aria-label="次月">▶</button>
      </div>
      <table class="calendar-table">
        <thead>
          <tr>
			  ${dayNames.map((d, i) => {
			    const cls = i === 0 ? 'sun' : i === 6 ? 'sat' : '';
			    return `<th class="${cls}">${d}</th>`;
			  }).join('')}
			</tr>
        </thead>
        <tbody><tr>
    `;

    // 空白の前日分
    for (let i = 0; i < firstDay; i++) html += `<td></td>`;

    // 日付部分
    for (let d = 1; d <= daysInMonth; d++) {
      const currentDay = new Date(year, month, d);
      const dayOfWeek = currentDay.getDay(); // 0(日)〜6(土)

      const isToday =
        d === today.getDate() &&
        month === today.getMonth() &&
        year === today.getFullYear();

      // 曜日クラス
      let weekdayClass = "";
      if (dayOfWeek === 0) weekdayClass = "sun";
      else if (dayOfWeek === 6) weekdayClass = "sat";

      // today クラスとの結合
      const classList = [isToday ? "today" : "", weekdayClass].filter(Boolean).join(" ");

      html += `<td class="${classList}" data-date="${year}-${month + 1}-${d}">${d}</td>`;
      if ((firstDay + d) % 7 === 0) html += `</tr><tr>`;
    }

    html += `</tr></tbody></table>`;
    calendarEl.innerHTML = html;

    // 月切替
    document.getElementById("prevMonth").onclick = () => {
      current.setMonth(current.getMonth() - 1);
      renderCalendar(current);
    };
    document.getElementById("nextMonth").onclick = () => {
      current.setMonth(current.getMonth() + 1);
      renderCalendar(current);
    };

    // 日付クリック
    calendarEl.querySelectorAll("td[data-date]").forEach(td => {
      td.addEventListener("click", () => {
        const selectedDate = td.getAttribute("data-date");
        alert(`${selectedDate} を選択しました`);
      });
    });
  }

  renderCalendar(current);
});

// モーダル操作
function openModal() {
  document.getElementById("logoutModal").classList.remove("hidden");
}
function closeModal() {
  document.getElementById("logoutModal").classList.add("hidden");
}
