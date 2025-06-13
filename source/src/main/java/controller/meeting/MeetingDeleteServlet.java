package controller.meeting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.MeetingDao;

@WebServlet("/meeting/delete")
public class MeetingDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String meetingId = request.getParameter("id");
        try {
            MeetingDao dao = new MeetingDao();
            dao.delete(Integer.parseInt(meetingId));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "削除に失敗しました。");
        }
        response.sendRedirect(request.getContextPath() + "/meeting/list");
    }
}