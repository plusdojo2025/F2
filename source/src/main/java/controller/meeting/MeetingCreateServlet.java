package controller.meeting;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AuthenticatedServlet;
import model.dto.MeetingDto;
import model.dto.UserDto;
import model.service.MeetingService;

@WebServlet("/meeting/create")
public class MeetingCreateServlet extends AuthenticatedServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");

        System.out.println("=== [POST] 会議作成処理 開始 ===");

        try {
            String title = req.getParameter("title");
            String dateStr = req.getParameter("date");
            String timeInput = req.getParameter("time");
            String participants = req.getParameter("participants");
            String action = req.getParameter("action");

            System.out.println("[DEBUG] title = " + title);
            System.out.println("[DEBUG] date = " + dateStr);
            System.out.println("[DEBUG] time = " + timeInput);
            System.out.println("[DEBUG] participants = " + participants);
            System.out.println("[DEBUG] action = " + action);
            System.out.println("[DEBUG] createdBy = " + loginUser.getUserId());

            if (title == null || title.isEmpty() ||
                dateStr == null || dateStr.isEmpty() ||
                timeInput == null || timeInput.isEmpty() ||
                participants == null || participants.isEmpty()) {
                throw new IllegalArgumentException("全ての入力項目は必須です。");
            }

            // 柔軟な記号対応と空白除去
            timeInput = timeInput.replaceAll("[〜ー−－―‐-]", "~").replaceAll("\\s+", "");
            if (!timeInput.contains("~")) {
                throw new IllegalArgumentException("時間の形式が不正です（例：9:00~11:00）");
            }

            String[] timeParts = timeInput.split("~");
            if (timeParts.length != 2) {
                throw new IllegalArgumentException("時間は「開始~終了」の形式にしてください。");
            }

            String startTimeStr = normalizeTime(timeParts[0].trim());
            String endTimeStr = normalizeTime(timeParts[1].trim());

            Time startTime = Time.valueOf(startTimeStr);
            Time endTime = Time.valueOf(endTimeStr);

            MeetingDto dto = new MeetingDto();
            dto.setTitle(title);
            dto.setMeetingDate(Date.valueOf(dateStr));
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setParticipantsText(participants);
            dto.setCreatedBy(loginUser.getUserId());
            dto.setDetailArea("");  // 空文字で初期化
            dto.setDecisions("");   // 空文字で初期化

            System.out.println("[DEBUG] DTO作成完了: " + dto.getTitle() + ", " + dto.getMeetingDate());

            MeetingService service = new MeetingService();
            int meetingId = service.createMeeting(dto);  // ← 修正ポイント

            System.out.println("[INFO] 会議作成成功: meetingId = " + meetingId);

            if ("create".equals(action)) {
                resp.sendRedirect(req.getContextPath() + "/meeting/list");
            } else if ("next".equals(action)) {
                resp.sendRedirect(req.getContextPath() + "/speech/register?meetingId=" + meetingId);
            } else {
                throw new IllegalArgumentException("不正なアクションです: " + action);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ERROR] 登録エラー: " + e.getMessage());
            req.setAttribute("error", "会議の登録中にエラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingForm.jsp").forward(req, resp);
        }
    }

    private String normalizeTime(String timeStr) {
        if (timeStr.matches("^\\d{1,2}:\\d{1,2}$")) {
            String[] parts = timeStr.split(":");
            String hh = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
            String mm = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
            return hh + ":" + mm + ":00";
        } else if (timeStr.matches("^\\d{2}:\\d{2}:\\d{2}$")) {
            return timeStr;
        } else {
            throw new IllegalArgumentException("時間の形式が不正です。例：13:00、1:30、9:5 など");
        }
    }
}
