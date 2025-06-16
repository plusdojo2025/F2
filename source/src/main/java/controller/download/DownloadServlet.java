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

import model.join.MeetingDetailsDto;
import model.service.DownloadService;

/**
 * 議事録のダウンロードを担当するサーブレット。
 * ファイル形式は PDF または TEXT を指定可能。
 */
@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final DownloadService downloadService = new DownloadService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String meetingIdParam = request.getParameter("meetingId");
        String format = request.getParameter("format"); // "pdf" or "text"

        if (meetingIdParam == null || format == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが不足しています。");
            return;
        }

        int meetingId;
        try {
            meetingId = Integer.parseInt(meetingIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "meetingIdは数値で指定してください。");
            return;
        }

        // 一時ファイルのパス設定（ファイル名例: minutes_123.pdf）
        String tempDir = System.getProperty("java.io.tmpdir");
        String extension = format.equalsIgnoreCase("pdf") ? ".pdf" : ".txt";
        String fileName = "minutes_" + meetingId + extension;
        File tempFile = new File(tempDir, fileName);

        // 会議詳細の取得
        MeetingDetailsDto meetingDetails = downloadService.generatePreviewData(meetingId);
        if (meetingDetails == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "該当の会議が見つかりませんでした。");
            return;
        }

        boolean success = false;
        if ("pdf".equalsIgnoreCase(format)) {
            success = downloadService.exportToPDF(meetingDetails, tempFile.getAbsolutePath());
            response.setContentType("application/pdf");
        } else if ("text".equalsIgnoreCase(format)) {
            success = downloadService.exportToText(meetingDetails, tempFile.getAbsolutePath());
            response.setContentType("text/plain");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "形式はpdfまたはtextのみ指定可能です。");
            return;
        }

        if (!success || !tempFile.exists()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ファイルの生成に失敗しました。");
            return;
        }

        // レスポンスヘッダ設定
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLengthLong(tempFile.length());

        // ファイル内容をレスポンスに書き出す
        try (FileInputStream fis = new FileInputStream(tempFile);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } finally {
            // 一時ファイルを削除
            tempFile.delete();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 検索条件取得
        String searchName = request.getParameter("searchName");
        String searchDate = request.getParameter("searchDate");

        request.setAttribute("searchName", searchName);
        request.setAttribute("searchDate", searchDate);

        // 検索結果取得
        List<MeetingSummaryDto> meetingList = downloadService.searchMeetings(searchName, searchDate);
        request.setAttribute("meetingList", meetingList);

        // JSP へフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/download.jsp").forward(request, response);
    }


}
