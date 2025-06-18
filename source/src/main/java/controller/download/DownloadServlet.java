package controller.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String meetingIdStr = request.getParameter("meeting");
        String format = request.getParameter("format");

        if (meetingIdStr == null || meetingIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "会議を選択してください。");
            return;
        }

        int meetingId;
        try {
            meetingId = Integer.parseInt(meetingIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "会議IDが不正です。");
            return;
        }

        DownloadService service = new DownloadService();

        try {
            MinutesManagementAndOutputDto dto = service.getMeetingDetails(meetingId);
            if (dto == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "会議が見つかりません。");
                return;
            }

            if ("text".equals(format)) {
                // テキスト形式で出力
                File textFile = service.generateMinutesTextFile(dto);
                response.setContentType("text/plain; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + textFile.getName() + "\"");
                response.setContentLengthLong(textFile.length());

                try (ServletOutputStream out = response.getOutputStream();
                     FileInputStream fis = new FileInputStream(textFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                }
                // textFile.delete(); // 必要なら即削除
            } else if ("pdf".equals(format)) {
                // TODO: PDF出力処理を実装（今は未対応の例外を出す）
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "PDF出力は未実装です。");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な出力形式です。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "出力処理に失敗しました。");
        }
    }



}
