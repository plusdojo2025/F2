package controller.meeting;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.MeetingDao;
import model.dto.MeetingDto;

@WebServlet("/meeting/edit")
public class MeetingEditServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/meeting/list");
            return;
        }
        int id = Integer.parseInt(idStr);
        MeetingDao dao = new MeetingDao();
        MeetingDto meeting = dao.findById(id);
        if (meeting == null) {
            request.setAttribute("error", "会議が見つかりませんでした。");
        } else {
            request.setAttribute("meeting", meeting);
        }
        request.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingEdit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int meetingId = Integer.parseInt(request.getParameter("meetingId"));
            String title = request.getParameter("title");
            Date meetingDate = Date.valueOf(request.getParameter("meetingDate"));
            Time startTime = Time.valueOf(request.getParameter("startTime"));
            Time endTime = Time.valueOf(request.getParameter("endTime"));
            String participantsText = request.getParameter("participantsText");

            MeetingDao dao = new MeetingDao();
            MeetingDto meeting = dao.findById(meetingId);
            if (meeting != null) {
                meeting.setTitle(title);
                meeting.setMeetingDate(meetingDate);
                meeting.setStartTime(startTime);
                meeting.setEndTime(endTime);
                meeting.setParticipantsText(participantsText);
                dao.update(meeting);
            }
            response.sendRedirect(request.getContextPath() + "/meeting/list");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "更新に失敗しました。");
            doGet(request, response);
        }
    }
}