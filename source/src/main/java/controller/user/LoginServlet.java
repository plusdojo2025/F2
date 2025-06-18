package controller.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dto.UserDto;
import model.service.UserService;


@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request, response);
    }
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
	
		
	try {
		UserService userService = new UserService();
		UserDto user = userService.login(email, password);
		
		if (user != null) {
			request.getSession().setAttribute("loginUser",user);
			// ダッシュボードJSPが存在すればリダイレクト、なければ404
			String dashboardPath = "/WEB-INF/jsp/dashboard.jsp";
			java.io.File dashboardFile = new java.io.File(getServletContext().getRealPath(dashboardPath));
			if (dashboardFile.exists()) {
				response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Dashboard not found");
			}
		} else {
			request.setAttribute("errorMessage", "メールアドレスまたはパスワードが違います。");
			request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request,response);
			
		}
		
	} catch (Exception e) {
		request.setAttribute("errorMessage", "ログインエラー："+ e.getMessage());
		request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request,response);
	}
	}
}
