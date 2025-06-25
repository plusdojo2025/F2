package controller.meeting;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthenticatedServlet;
import model.dao.AgendaDao;
import model.dao.MeetingDao;
import model.dto.AgendaDto;
import model.dto.MeetingDto;

@WebServlet("/meeting/detail")
public class MeetingDetailServlet extends AuthenticatedServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String format = req.getParameter("format");
        
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/meeting/list");
            return;
        }

        int id = Integer.parseInt(idStr);
        MeetingDao dao = new MeetingDao();
        MeetingDto meeting = dao.findById(id);
        // 議題リストも取得
        List<AgendaDto> agendas = null;
        try {
            AgendaDao agendaDao = new AgendaDao(dao.getConnection());
            agendas = agendaDao.findByMeetingId(id);
        } catch (SQLException e) {
            agendas = null;
        }

        if (meeting == null) {
            if ("json".equals(format)) {
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\": \"会議が見つかりませんでした。\"}");
            } else {
                resp.setContentType("text/html");
                resp.getWriter().write("<div class=\"error-message\">会議が見つかりませんでした。</div>");
            }
            return;
        }

        if ("json".equals(format)) {
            // 簡易詳細モーダル用のJSONデータ
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"meetingId\":").append(meeting.getMeetingId()).append(",");
            json.append("\"title\":\"").append(escapeJson(meeting.getTitle())).append("\",");
            json.append("\"meetingDate\":\"").append(meeting.getMeetingDate()).append("\",");
            json.append("\"startTime\":\"").append(meeting.getStartTime()).append("\",");
            json.append("\"endTime\":\"").append(meeting.getEndTime()).append("\",");
            json.append("\"createdBy\":").append(meeting.getCreatedBy()).append(",");
            json.append("\"participantsText\":\"").append(escapeJson(meeting.getParticipantsText())).append("\",");
            json.append("\"isDeleted\":").append(meeting.isDeleted()).append(",");
            json.append("\"createdAt\":\"").append(meeting.getCreatedAt()).append("\",");
            json.append("\"updatedAt\":\"").append(meeting.getUpdatedAt()).append("\",");
            json.append("\"detailArea\":\"").append(escapeJson(meeting.getDetailArea())).append("\",");
            json.append("\"decisions\":\"").append(escapeJson(meeting.getDecisions())).append("\",");
            // agendas配列を追加
            json.append("\"agendas\":[");
            if (agendas != null && !agendas.isEmpty()) {
                for (int i = 0; i < agendas.size(); i++) {
                    AgendaDto a = agendas.get(i);
                    json.append("{");
                    json.append("\"agendaId\":").append(a.getAgendaId()).append(",");
                    json.append("\"title\":\"").append(escapeJson(a.getTitle())).append("\",");
                    json.append("\"description\":\"").append(escapeJson(a.getDescription())).append("\",");
                    json.append("\"orderNumber\":").append(a.getOrderNumber()).append(",");
                    json.append("\"speechNote\":\"").append(escapeJson(a.getSpeechNote())).append("\",");
                    json.append("\"decisionNote\":\"").append(escapeJson(a.getDecisionNote())).append("\"");
                    json.append("}");
                    if (i < agendas.size() - 1) json.append(",");
                }
            }
            json.append("]");
            json.append("}");
            resp.getWriter().write(json.toString());
        } else {
            // 詳細モーダル用のHTMLデータ
            resp.setContentType("text/html");
            req.setAttribute("meeting", meeting);
            req.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingDetailContent.jsp")
                  .forward(req, resp);
        }
    }

    // JSONの特殊文字をエスケープするメソッド
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
}