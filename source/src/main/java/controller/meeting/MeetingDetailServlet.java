package controller.meeting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.MeetingDao;
import model.dto.MeetingDto;

@WebServlet("/meeting/detail")
public class MeetingDetailServlet extends HttpServlet {
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
        request.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingDetail.jsp").forward(request, response);
    }
}