package controller.meeting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.MeetingDao;
import model.dto.MeetingDto;

@WebServlet("/meeting/list")
public class MeetingSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MeetingDao dao = new MeetingDao();
        List<MeetingDto> meetings = dao.findAll();
        System.out.println("meetings.size() = " + meetings.size());
        request.setAttribute("meetings", meetings);
        request.getRequestDispatcher("/WEB-INF/jsp/meeting/meetingList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}