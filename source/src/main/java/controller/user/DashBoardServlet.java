package controller.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dto.UserDto;

/**
 * ダッシュボード画面表示を担当するサーブレット
 */

@WebServlet("/dashboard")
public class DashBoardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println("DashBoardServletに到達しました");


        // セッションからログインユーザーを取得
        HttpSession session = request.getSession(false);
        UserDto user = (session != null) ? (UserDto) session.getAttribute("loginUser") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ユーザー情報をリクエストに渡してJSPへ
        request.setAttribute("loginUser", user);
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
    }
}
