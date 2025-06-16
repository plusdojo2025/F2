package controller.meeting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dto.MeetingDto;
import model.service.MeetingService;

@WebServlet("/meeting/list")
public class MeetingListServlet extends HttpServlet {
    private MeetingService meetingService;

    @Override
    public void init() {
        meetingService = new MeetingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String meetingName = request.getParameter("meetingName");
            String meetingDate = request.getParameter("meetingDate");
            String sort = request.getParameter("sort");

            // ページ番号取得
            int page = 1;
            int pageSize = 9;
            if (request.getParameter("page") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("page"));
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

            request.setAttribute("meetings", meetings);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("meetingName", meetingName);
            request.setAttribute("meetingDate", meetingDate);
            request.setAttribute("sort", sort);

            request.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingList.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}