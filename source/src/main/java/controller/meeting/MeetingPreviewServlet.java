package controller.meeting;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dto.AgendaDto;
import model.dto.MeetingDto;
import model.service.MeetingService;


/**
 * 議事録の出力プレビュー画面表示を担当するサーブレット。
 */
@WebServlet("/meeting/preview")
public class MeetingPreviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // パラメータから会議IDを取得
        	String meetingIdParam = request.getParameter("meetingId");
            if (meetingIdParam == null || meetingIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "会議IDが指定されていません。");
                return;
            }

            int meetingId = Integer.parseInt(meetingIdParam);

            // 会議詳細を取得
            MeetingService meetingService = new MeetingService();
            MeetingDto meeting = meetingService.findMeetingById(meetingId);

            if (meeting == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された会議が見つかりませんでした。");
                return;
            }

            // 議題リストも取得
            var agendas = meetingService.findAgendasByMeetingId(meetingId);

            // JSPに渡す
            request.setAttribute("meeting", meeting);
            request.setAttribute("agendas", agendas);
            request.getRequestDispatcher("/WEB-INF/jsp/download/preview.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "プレビューの取得中にエラーが発生しました。");
            // 404エラーを避けるため、会議一覧にリダイレクト
            response.sendRedirect(request.getContextPath() + "/meeting/list");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        MeetingService meetingService = new MeetingService();

        // 発言入力画面 or 編集画面からのリクエストの場合
        try {
            String meetingIdStr = request.getParameter("meetingId");
            if (meetingIdStr == null || meetingIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "会議IDが指定されていません。");
                return;
            }
            
            int meetingId = Integer.parseInt(meetingIdStr);
            
            // DBから既存の会議情報を取得
            MeetingDto meeting = meetingService.findMeetingById(meetingId);
            if (meeting == null) {
                throw new ServletException("指定された会議が見つかりません: " + meetingId);
            }
            
            // フォームに存在する項目のみで会議情報を更新
            String title = request.getParameter("title");
            if (title != null) {
                meeting.setTitle(title);
            }
            String dateStr = request.getParameter("date");
            if (dateStr != null && !dateStr.isEmpty()) {
                meeting.setMeetingDate(Date.valueOf(dateStr));
            }
            String startTimeStr = request.getParameter("startTime");
            if (startTimeStr != null && !startTimeStr.isEmpty()) {
                meeting.setStartTime(Time.valueOf(normalizeTime(startTimeStr)));
            }
            String endTimeStr = request.getParameter("endTime");
            if (endTimeStr != null && !endTimeStr.isEmpty()) {
                meeting.setEndTime(Time.valueOf(normalizeTime(endTimeStr)));
            }
            String participants = request.getParameter("participants");
            if (participants != null) {
                meeting.setParticipantsText(participants);
            }

            List<AgendaDto> agendas = new ArrayList<>();
            int i = 0;
            while (request.getParameter("agendas[" + i + "].title") != null) {
                AgendaDto agenda = new AgendaDto();
                agenda.setMeetingId(meetingId);

                String agendaIdStr = request.getParameter("agendas[" + i + "].agendaId");
                if (agendaIdStr != null && !agendaIdStr.isEmpty()) {
                    agenda.setAgendaId(Integer.parseInt(agendaIdStr));
                }

                agenda.setTitle(request.getParameter("agendas[" + i + "].title"));
                agenda.setSpeechNote(request.getParameter("agendas[" + i + "].speechNote"));
                agenda.setDecisionNote(request.getParameter("agendas[" + i + "].decisionNote"));
                agenda.setOrderNumber(i + 1);
                agendas.add(agenda);
                i++;
            }

            // 賢い保存メソッドを呼び出す
            if (!agendas.isEmpty()) {
                meetingService.updateMeetingAndAgendasSmart(meeting, agendas);
            } else if (title != null) { // 議題が無くても会議情報更新は行う
                meetingService.updateMeeting(meeting);
            }
            
            // 保存後、再度データを取得してプレビュー表示
            MeetingDto updatedMeeting = meetingService.findMeetingById(meetingId);
            List<AgendaDto> updatedAgendas = meetingService.findAgendasByMeetingId(meetingId);

            request.setAttribute("meeting", updatedMeeting);
            request.setAttribute("agendas", updatedAgendas);
            request.getRequestDispatcher("/WEB-INF/jsp/download/preview.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "プレビューの表示中にエラーが発生しました: " + e.getMessage());
            // 404エラーを避けるため、会議一覧にリダイレクト
            response.sendRedirect(request.getContextPath() + "/meeting/list?error=preview_failed");
        }
    }
    
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