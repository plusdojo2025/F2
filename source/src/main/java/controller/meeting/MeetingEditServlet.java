package controller.meeting;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dto.AgendaDto;
import model.dto.MeetingDto;
import model.service.MeetingService;

@WebServlet("/meeting/edit")
public class MeetingEditServlet extends HttpServlet {

    private final MeetingService meetingService = new MeetingService();

    // 編集フォーム表示
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        try {
            String idStr = request.getParameter("id");
            if (idStr == null) {
                // 新規作成の場合もトークンを生成
                String token = UUID.randomUUID().toString();
                session.setAttribute("csrfToken", token);
                request.setAttribute("csrfToken", token);
                
                request.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingForm.jsp").forward(request, response);
                return;
            }

            int meetingId = Integer.parseInt(idStr);
            MeetingDto meeting = meetingService.findMeetingById(meetingId);

            if (meeting == null) {
                request.setAttribute("error", "会議が見つかりませんでした。");
            } else {
                request.setAttribute("meeting", meeting);
                request.setAttribute("agendas", meetingService.findAgendasByMeetingId(meetingId));
                
                // 編集時にもトークンを生成
                String token = UUID.randomUUID().toString();
                session.setAttribute("csrfToken", token);
                request.setAttribute("csrfToken", token);
            }

            request.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingForm.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "データ取得に失敗しました。");
            request.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingForm.jsp").forward(request, response);
        }
    }

    // 会議＋議題の更新処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // トークンチェック
        String sessionToken = (session != null) ? (String) session.getAttribute("csrfToken") : null;
        String requestToken = request.getParameter("csrfToken");

        if (sessionToken == null || !sessionToken.equals(requestToken)) {
            request.setAttribute("error", "不正なリクエストです。");
            // 404エラーを避けるため、会議一覧にリダイレクト
            response.sendRedirect(request.getContextPath() + "/meeting/list?error=invalid_request");
            return;
        }
        // 使用済みトークンを無効化
        session.removeAttribute("csrfToken");

        try {
            String meetingIdStr = request.getParameter("meetingId");
            boolean isCreate = (meetingIdStr == null || meetingIdStr.isEmpty());
            
            MeetingDto meeting = new MeetingDto();
            List<AgendaDto> agendas = new ArrayList<>();

            if (isCreate) {
                // 会議作成の場合
                String title = request.getParameter("title");
                String dateStr = request.getParameter("date");
                String timeInput = request.getParameter("time");
                String participants = request.getParameter("participants");
                String action = request.getParameter("action");

                if (title == null || title.isEmpty() ||
                    dateStr == null || dateStr.isEmpty() ||
                    timeInput == null || timeInput.isEmpty() ||
                    participants == null || participants.isEmpty()) {
                    throw new IllegalArgumentException("全ての入力項目は必須です。");
                }

                // 時間を整形して変換
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

                meeting.setTitle(title);
                meeting.setMeetingDate(Date.valueOf(dateStr));
                meeting.setStartTime(Time.valueOf(startTimeStr));
                meeting.setEndTime(Time.valueOf(endTimeStr));
                meeting.setParticipantsText(participants);
                meeting.setDetailArea("");
                meeting.setDecisions("");

                // ログインユーザーを取得
                if (session != null && session.getAttribute("loginUser") != null) {
                    meeting.setCreatedBy(((model.dto.UserDto) session.getAttribute("loginUser")).getUserId());
                } else {
                    throw new IllegalStateException("ログインユーザーが見つかりません。");
                }

                // 会議を作成
                int newMeetingId = meetingService.createMeeting(meeting);
                meeting.setMeetingId(newMeetingId);

                // 議題データを処理
                int i = 0;
                while (true) {
                    String agendaTitle = request.getParameter("agendas[" + i + "].title");

                    if (agendaTitle == null) {
                        break;
                    }

                    if (agendaTitle.trim().isEmpty()) {
                        i++;
                        continue;
                    }

                    AgendaDto agenda = new AgendaDto();
                    agenda.setMeetingId(newMeetingId);

                    String agendaIdStr = request.getParameter("agendas[" + i + "].agendaId");
                    if (agendaIdStr != null && !agendaIdStr.isEmpty()) {
                        agenda.setAgendaId(Integer.parseInt(agendaIdStr));
                    }

                    agenda.setTitle(agendaTitle);
                    agenda.setSpeechNote(request.getParameter("agendas[" + i + "].speechNote"));
                    agenda.setDecisionNote(request.getParameter("agendas[" + i + "].decisionNote"));
                    agenda.setOrderNumber(agendas.size() + 1);
                    agendas.add(agenda);
                    i++;
                }

                if (!agendas.isEmpty()) {
                    meetingService.addAgendaList(agendas);
                }

                if ("create".equals(action)) {
                    response.sendRedirect(request.getContextPath() + "/meeting/list");
                } else if ("next".equals(action)) {
                    response.sendRedirect(request.getContextPath() + "/speech/register?meetingId=" + newMeetingId);
                } else {
                    response.sendRedirect(request.getContextPath() + "/meeting/list");
                }

            } else {
                // 会議編集の場合
                System.out.println("=== [POST] 会議編集処理 開始 ===");
                int meetingId = Integer.parseInt(meetingIdStr);
                System.out.println("[DEBUG] meetingId = " + meetingId);

                // 時間を整形して変換
                String startTimeStr = normalizeTime(request.getParameter("startTime"));
                String endTimeStr = normalizeTime(request.getParameter("endTime"));

                meeting.setMeetingId(meetingId);
                meeting.setTitle(request.getParameter("title"));
                meeting.setMeetingDate(Date.valueOf(request.getParameter("date")));
                meeting.setStartTime(Time.valueOf(startTimeStr));
                meeting.setEndTime(Time.valueOf(endTimeStr));
                meeting.setParticipantsText(request.getParameter("participants"));
                meeting.setDetailArea("");  // 空文字で初期化
                meeting.setDecisions("");   // 空文字で初期化

                System.out.println("[DEBUG] 会議情報設定完了: " + meeting.getTitle());

                // 議題データを処理
                int i = 0;
                while (true) {
                    String agendaTitle = request.getParameter("agendas[" + i + "].title");

                    if (agendaTitle == null) {
                        break;
                    }

                    if (agendaTitle.trim().isEmpty()) {
                        i++;
                        continue;
                    }

                    AgendaDto agenda = new AgendaDto();
                    agenda.setMeetingId(meetingId);

                    String agendaIdStr = request.getParameter("agendas[" + i + "].agendaId");
                    if (agendaIdStr != null && !agendaIdStr.isEmpty()) {
                        agenda.setAgendaId(Integer.parseInt(agendaIdStr));
                    }

                    agenda.setTitle(agendaTitle);
                    agenda.setSpeechNote(request.getParameter("agendas[" + i + "].speechNote"));
                    agenda.setDecisionNote(request.getParameter("agendas[" + i + "].decisionNote"));
                    agenda.setOrderNumber(agendas.size() + 1);
                    agendas.add(agenda);
                    i++;
                }

                System.out.println("[DEBUG] 議題データ数: " + agendas.size());

                // 議題データがある場合は議題も含めて更新、ない場合は会議情報のみ更新
                if (!agendas.isEmpty()) {
                    System.out.println("[DEBUG] 会議＋議題を更新");
                    meetingService.updateMeetingAndAgendasSmart(meeting, agendas);
                } else {
                    System.out.println("[DEBUG] 会議情報のみ更新");
                    meetingService.updateMeeting(meeting);
                }
                
                System.out.println("[INFO] 会議編集成功: meetingId = " + meetingId);
                response.sendRedirect(request.getContextPath() + "/meeting/list");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "保存に失敗しました: " + e.getMessage());
            doGet(request, response);
        }
    }

    // 時刻を HH:mm:00 形式に補完する
    private String normalizeTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            throw new IllegalArgumentException("時間が未入力です");
        }

        if (timeStr.matches("^\\d{1,2}:\\d{1,2}$")) {
            String[] parts = timeStr.split(":");
            String hh = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
            String mm = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
            return hh + ":" + mm + ":00";
        } else if (timeStr.matches("^\\d{2}:\\d{2}:\\d{2}$")) {
            return timeStr;
        } else {
            throw new IllegalArgumentException("時間の形式が不正です（例：10:00 または 10:00:00）");
        }
    }
}
