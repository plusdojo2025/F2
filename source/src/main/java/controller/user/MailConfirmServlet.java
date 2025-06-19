package controller.user;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/mail-confirm")
public class MailConfirmServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/user/mailConfirm.jsp").forward(request, response);
    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		if ("send-code".equals(action)) {
			// 認証コード送信処理（ダミー）
			handleSendCode(request, response);
		} else if ("verify-code".equals(action)) {
			// 認証コード確認処理（ダミー）
			handleVerifyCode(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/mail-confirm");
		}
	}
	
	private void handleSendCode(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String email = request.getParameter("email");
		
		if (email == null || email.trim().isEmpty()) {
			request.setAttribute("errorMessage", "メールアドレスを入力してください。");
			request.getRequestDispatcher("/WEB-INF/jsp/user/mailConfirm.jsp").forward(request, response);
			return;
		}
		
		// ダミーの認証コード生成（6桁の数字）
		String verificationCode = generateVerificationCode();
		
		// セッションに認証コードを保存
		HttpSession session = request.getSession();
		session.setAttribute("verificationCode", verificationCode);
		session.setAttribute("email", email);
		
		// ダミーのメール送信処理（実際には送信しない）
		System.out.println("=== メール認証コード送信（ダミー） ===");
		System.out.println("送信先: " + email);
		System.out.println("認証コード: " + verificationCode);
		System.out.println("================================");
		
		request.setAttribute("successMessage", "認証コードを送信しました。");
		request.setAttribute("email", email);
		request.getRequestDispatcher("/WEB-INF/jsp/user/mailConfirm.jsp").forward(request, response);
	}
	
	private void handleVerifyCode(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String inputCode = request.getParameter("verificationCode");
		HttpSession session = request.getSession();
		String storedCode = (String) session.getAttribute("verificationCode");
		String email = (String) session.getAttribute("email");
		
		if (storedCode == null || email == null) {
			request.setAttribute("errorMessage", "認証コードの有効期限が切れています。再度送信してください。");
			request.getRequestDispatcher("/WEB-INF/jsp/user/mailConfirm.jsp").forward(request, response);
			return;
		}
		
		if (inputCode != null && inputCode.equals(storedCode)) {
			// 認証成功
			session.removeAttribute("verificationCode");
			session.setAttribute("emailVerified", true);
			session.setAttribute("verifiedEmail", email);
			
			request.setAttribute("successMessage", "メール認証が完了しました！");
			// 認証完了後は登録ページにリダイレクト
			response.sendRedirect(request.getContextPath() + "/register");
		} else {
			// 認証失敗
			request.setAttribute("errorMessage", "認証コードが正しくありません。");
			request.setAttribute("email", email);
			request.getRequestDispatcher("/WEB-INF/jsp/user/mailConfirm.jsp").forward(request, response);
		}
	}
	
	private String generateVerificationCode() {
		Random random = new Random();
		int code = random.nextInt(900000) + 100000; // 100000-999999の6桁
		return String.valueOf(code);
	}
}
