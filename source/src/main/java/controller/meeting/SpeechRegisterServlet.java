package controller.meeting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dto.AgendaDto;
import model.dto.MeetingDto;
import model.service.MeetingService;

/**
 * 発言・議題登録処理を担当するサーブレット（複数議題対応の最終版）
 * 入力値を取得して MeetingService に委譲し、プレビュー画面へ遷移する
 */
@WebServlet("/speech/register")
public class SpeechRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final MeetingService meetingService = new MeetingService();

	/**
	 * 発言登録画面の表示（GETリクエスト）
	 * 会議情報を取得してJSPに渡す
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int meetingId = Integer.parseInt(request.getParameter("meetingId"));
			MeetingDto meeting = meetingService.findMeetingById(meetingId);
			if (meeting == null) {
				response.sendRedirect(request.getContextPath() + "/meeting/list?error=not_found");
				return;
			}
			request.setAttribute("meeting", meeting);
			request.getRequestDispatcher("/WEB-INF/jsp/meeting/speechForm.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/meeting/list?error=invalid_id");
		}
	}

	/**
	 * 発言・議題の登録処理（POSTリクエスト）
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String meetingIdParam = request.getParameter("meetingId");

		try {
			int meetingId = Integer.parseInt(meetingIdParam);
			
			List<AgendaDto> agendas = new ArrayList<>();
			int i = 0;
			while (request.getParameter("agendas[" + i + "].title") != null) {
				String title = request.getParameter("agendas[" + i + "].title");
				
				// タイトルが空の議題は無視する
				if (title == null || title.trim().isEmpty()) {
					i++;
					continue;
				}

				AgendaDto agenda = new AgendaDto();
				agenda.setMeetingId(meetingId);
				agenda.setTitle(title);
				agenda.setSpeechNote(request.getParameter("agendas[" + i + "].speechNote"));
				agenda.setDecisionNote(request.getParameter("agendas[" + i + "].decisionNote"));
				agenda.setOrderNumber(i + 1);
				agendas.add(agenda);
				i++;
			}
			
			// 有効な議題が1つもなければエラーメッセージと共にフォームに戻る
			if (agendas.isEmpty()) {
				request.setAttribute("error", "最低1つの有効な議題（タイトル必須）を入力してください。");
				doGet(request, response);
				return;
			}
			
			meetingService.addAgendaList(agendas);
			
			// 登録完了後、プレビュー画面へリダイレクト
			response.sendRedirect(request.getContextPath() + "/meeting/preview?meetingId=" + meetingId);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "発言の保存に失敗しました。");
			// エラー発生時、再度フォームを表示するためにdoGetを呼び出す
			doGet(request, response);
		}
	}
}
