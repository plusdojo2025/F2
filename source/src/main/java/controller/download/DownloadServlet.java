package controller.download;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dto.MinutesManagementAndOutputDto;
import model.service.DownloadService;

/**
 * 会議検索を処理するサーブレット。
 */
@WebServlet({"/download", "/download/searchMeetings", "/controller.download.DownloadServlet"})
public class DownloadServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;

    /**
     * GETリクエストを処理し、検索条件に応じた会議一覧をJSONで返却する。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	String path = request.getServletPath();
    	if ("/download".equals(path)) {
            // JSPへフォワード（画面表示）
            request.getRequestDispatcher("/WEB-INF/jsp/download/download.jsp").forward(request, response);
            return;  // ここで処理終了
        }


        // リクエストパラメータ取得（会議名と日付）
        String searchName = request.getParameter("name");
        String searchDate = request.getParameter("date");

        // 会議検索サービスの呼び出し
        DownloadService service = new DownloadService();
        List<MinutesManagementAndOutputDto> meetings = service.searchMeetings(searchName, searchDate);

        // レスポンス設定
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // JSON手動構築（Gson等は使わない）
        StringBuilder json = new StringBuilder();
        json.append("[");

        if (meetings != null && !meetings.isEmpty()) {
            for (int i = 0; i < meetings.size(); i++) {
                MinutesManagementAndOutputDto dto = meetings.get(i);
                json.append("{");
                json.append("\"meetingId\":").append(dto.getmeeting_id()).append(",");
                json.append("\"title\":\"").append(escapeJson(dto.getTitle())).append("\",");
                json.append("\"meetingDate\":\"").append(dto.getMeetingDate()).append("\"");
                json.append("}");
                if (i < meetings.size() - 1) {
                    json.append(",");
                }
            }
        }

        json.append("]");
        out.print(json.toString());
        out.flush();
    }

    /**
     * JSONの特殊文字をエスケープする補助メソッド
     */
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
