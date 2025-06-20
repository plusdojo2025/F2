package controller.download;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dto.MinutesManagementAndOutputDto;
import model.service.DownloadService;

@WebServlet("/download/preview")
public class DownloadPreviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String meetingIdStr = request.getParameter("meeting");
		String format = request.getParameter("format");

		if (meetingIdStr == null || format == null || meetingIdStr.isEmpty() || format.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "会議IDまたは出力形式が未指定です。");
			return;
		}

		try {
			int meetingId = Integer.parseInt(meetingIdStr);

			DownloadService service = new DownloadService();
			 // dtoとテキストを取得
	        MinutesManagementAndOutputDto dto = service.getMeetingDetails(meetingId);
	        String previewText = service.getMinutesPreviewText(meetingId, format);

			if (dto == null || previewText == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "会議情報が見つかりません。");
				return;
			}

			// JSPに渡す
			request.setAttribute("dto", dto);
			request.setAttribute("meetingId", meetingId);
			request.setAttribute("format", format);
			request.setAttribute("previewText", previewText);

			// プレビューページへ遷移
			request.getRequestDispatcher("/WEB-INF/jsp/download/downloadPreview.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "会議IDの形式が正しくありません。");
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB接続エラーが発生しました。");
		} catch (UnsupportedOperationException e) {
			response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "選択された形式にはまだ対応していません。");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "予期しないエラーが発生しました。");
		}
	}
}