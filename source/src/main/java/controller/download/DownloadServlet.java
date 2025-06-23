package controller.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dto.MeetingDetailsDto;
import model.dto.MinutesManagementAndOutputDto;
import model.service.DownloadService;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final DownloadService downloadService = new DownloadService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mode = request.getParameter("mode");

        // --- JSON検索APIモード ---
        if ("api".equals(mode)) {
            String name = request.getParameter("name");
            String date = request.getParameter("date");

            List<MinutesManagementAndOutputDto> list = downloadService.searchMeetings(name, date);

            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(buildJson(list));
            return;
        }

        // --- JSP表示（通常画面） ---
        String name = request.getParameter("searchName");
        String date = request.getParameter("searchDate");

        List<MinutesManagementAndOutputDto> meetingList = downloadService.searchMeetings(name, date);
        request.setAttribute("meetingList", meetingList);
        
        // 検索条件をJSPに渡す
        request.setAttribute("searchName", name);
        request.setAttribute("searchDate", date);

        request.getRequestDispatcher("/WEB-INF/jsp/download/download.jsp").forward(request, response);
    }

    private String buildJson(List<MinutesManagementAndOutputDto> list) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            MinutesManagementAndOutputDto dto = list.get(i);
            json.append("{")
                .append("\"meetingId\":").append(dto.getmeeting_id()).append(",")
                .append("\"title\":\"").append(escape(dto.getTitle())).append("\",")
                .append("\"meetingDate\":\"").append(dto.getMeetingDate()).append("\"")
                .append("}");
            if (i < list.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    private String escape(String input) {
        if (input == null) return "";
        return input.replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "")
                    .replace("\t", "\\t");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ 開発用：仮ユーザーIDをセッションに設定
        if (request.getSession().getAttribute("loginUserId") == null) {
            request.getSession().setAttribute("loginUserId", 1);
        }

        // --- ログイン確認 ---
        HttpSession session = request.getSession(false);
        Integer loginUserId = (session != null) ? (Integer) session.getAttribute("loginUserId") : null;

        if (loginUserId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ログイン情報が見つかりません。");
            return;
        }

        // --- パラメータ取得 ---
        String idStr = request.getParameter("meetingId");
        String format = request.getParameter("format");

        if (idStr == null || format == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが不足しています。");
            return;
        }

        int meetingId;
        try {
            meetingId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "会議IDが不正です。");
            return;
        }

        // --- 会議情報の取得 ---
        MeetingDetailsDto meeting = downloadService.generatePreviewData(meetingId, loginUserId);
        if (meeting == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "会議が見つかりません。");
            return;
        }

        meeting.setCreatedBy(loginUserId);

        // --- 出力ファイル準備 ---
        String ext = format.equalsIgnoreCase("pdf") ? ".pdf" : ".txt";
        String fileName = "minutes_" + meetingId + ext;
        File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);

        boolean success = false;
        if ("pdf".equalsIgnoreCase(format)) {
            // ✅ フォント指定に対応した修正済み呼び出し
            success = downloadService.exportToPDF(meeting, tempFile.getAbsolutePath(), getServletContext());
            response.setContentType("application/pdf");
        } else if ("text".equalsIgnoreCase(format)) {
            success = downloadService.exportToText(meeting, tempFile.getAbsolutePath());
            response.setContentType("text/plain; charset=UTF-8");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "形式はpdfまたはtextのみです。");
            return;
        }

        if (!success || !tempFile.exists()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ファイルの生成に失敗しました。");
            return;
        }

        // --- ダウンロードレスポンス ---
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLengthLong(tempFile.length());

        try (FileInputStream fis = new FileInputStream(tempFile);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } finally {
            tempFile.delete(); // 一時ファイル削除
        }
    }
}
