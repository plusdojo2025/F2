package controller.meeting;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dto.AgendaDto;
import model.service.MeetingService;

/**
 * 発言・議題登録処理を担当するサーブレット
 * 入力値を取得して MeetingService に委譲する
 */
@WebServlet("/speech/register")
public class SpeechRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 発言登録画面の表示（GETリクエスト）
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/meeting/speechForm.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * 発言・議題の登録処理（POSTリクエスト）
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータを取得する
		
		request.setCharacterEncoding("UTF-8");
		try {
			int meetingId = Integer.parseInt(request.getParameter("meetingId"));
			String title = request.getParameter("title");
			String speech = request.getParameter("speech");
			String decision = request.getParameter("decision");
			
			// AgendaDto にまとめて処理
	        AgendaDto agenda = new AgendaDto();
	        agenda.setMeetingId(meetingId);
	        agenda.setTitle(title);
	        agenda.setSpeechNote(speech);
	        agenda.setDecisionNote(decision);
			
	        MeetingService service = new MeetingService();
	        service.addAgenda(agenda);
	        
	        // 登録後に会議詳細画面へ遷移（meetingId付き）
	        response.sendRedirect(request.getContextPath() + "/download/preview?meetingId=" + meetingId);
		}
		
		catch (Exception e) {
	        request.setAttribute("errorMessage", "発言登録に失敗しました：" + e.getMessage());
	        request.getRequestDispatcher("/WEB-INF/jsp/meeting/speechForm.jsp")
	               .forward(request, response);
	    }
	}
}
