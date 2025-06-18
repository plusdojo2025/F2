package controller.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dto.UserDto;
import model.service.UserService;

@WebServlet("/settings")
public class UserSettingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		UserDto loginUser = (session != null) ? (UserDto) session.getAttribute("loginUser") : null;

		if (loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		request.setAttribute("user", loginUser);
		// JSPにフォワード
		request.getRequestDispatcher("/WEB-INF/jsp/user/settings.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		UserDto loginUser = (session != null) ? (UserDto) session.getAttribute("loginUser") : null;

		if (loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		request.setCharacterEncoding("UTF-8");

		String newUsername = request.getParameter("username");
		String newEmail = request.getParameter("email");
		String currentPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");

		if (!newPassword.equals(confirmPassword)) {
			request.setAttribute("error", "新しいパスワードが一致しません。");
			request.setAttribute("user", loginUser);
			request.getRequestDispatcher("/WEB-INF/jsp/user/settings.jsp").forward(request, response);
			return;
		}

		// DTO構築
		UserDto updateUser = new UserDto();
		updateUser.setUserId(loginUser.getUserId());
		updateUser.setUsername(newUsername);
		updateUser.setEmail(newEmail);
		updateUser.setCurrentPassword(currentPassword);
		updateUser.setNewPassword(newPassword);

		// 更新処理
		UserService service = new UserService();
		boolean success = service.updateUserAccount(updateUser);

		if (success) {
			// セッション更新
			loginUser.setUsername(newUsername);
			loginUser.setEmail(newEmail);
			session.setAttribute("loginUser", loginUser);
			request.setAttribute("message", "アカウント情報を更新しました。");
		} else {
			request.setAttribute("error", "現在のパスワードが正しくありません。");
		}

		request.setAttribute("user", loginUser);
		request.getRequestDispatcher("/WEB-INF/jsp/user/settings.jsp").forward(request, response);
	}

}
