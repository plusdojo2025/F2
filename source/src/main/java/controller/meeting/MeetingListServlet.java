package controller.meeting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthenticatedServlet;
import model.dto.MeetingDto;
import model.service.MeetingService;

@WebServlet("/meeting/list")
public class MeetingListServlet extends AuthenticatedServlet {
    private MeetingService meetingService;

    @Override
    public void init() {
        meetingService = new MeetingService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String meetingName = req.getParameter("meetingName");
            String meetingDate = req.getParameter("meetingDate");
            String sort = req.getParameter("sort");

            // ページ番号取得
            int page = 1;
            int pageSize = 9;
            if (req.getParameter("page") != null) {
                try {
                    page = Integer.parseInt(req.getParameter("page"));
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // 全件取得＆フィルタ・ソート
            List<MeetingDto> allMeetings = meetingService.findAllMeetings(meetingName, meetingDate, sort);
            int totalMeetings = allMeetings.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalMeetings);

            // ページ分割
            List<MeetingDto> meetings = (fromIndex < totalMeetings) ? allMeetings.subList(fromIndex, toIndex) : java.util.Collections.emptyList();
            int totalPages = (int) Math.ceil((double) totalMeetings / pageSize);

            req.setAttribute("meetings", meetings);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("meetingName", meetingName);
            req.setAttribute("meetingDate", meetingDate);
            req.setAttribute("sort", sort);

            req.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingList.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}