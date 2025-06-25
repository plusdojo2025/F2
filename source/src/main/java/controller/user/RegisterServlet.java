package controller.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dto.UserDto;
import model.service.UserService;
import util.Validator;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp").forward(request, response);
    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException,IOException {
		
		request.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		//入力チェック
		if (Validator.isEmpty(username) ||  
			!Validator.isWithinLength(username, 1, 100) ||
			!Validator.isEmail(email) ||
			!Validator.isPasswordComplex(password)) {
			
			request.setAttribute("errorMessage", "入力内容に不備があります。再度ご確認ください。");
			request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp").forward(request,response);
			return;
		}
		
		//Dto作成（パスワードはValidatorのハッシュ関数で暗号化）
		UserDto dto = new UserDto();
		dto.setUsername(username);
		dto.setEmail(email);
		dto.setPasswordHash(Validator.hashPassword(password));
		
		try {
			UserService service = new UserService();
			service.register(dto);
			response.sendRedirect(request.getContextPath() + "/login?registered=true");
		} catch (Exception e) {
			request.setAttribute("errorMessage","登録処理中にエラーが発生しました。"+ e.getMessage());
			request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp").forward(request, response);
		}
	}
	
}