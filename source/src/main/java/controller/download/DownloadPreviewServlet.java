package controller.download;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dto.MeetingDetailsDto;
import model.dto.MinutesManagementAndOutputDto;
import model.dto.UserDto;
import model.service.DownloadService;
import util.download.TextDownloader;

/**
 * 議事録の出力プレビュー画面表示を担当するサーブレット。
 * PDFプレビューとTextプレビュー、テキスト出力に対応。
 */
@WebServlet("/preview")
public class DownloadPreviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final DownloadService downloadService = new DownloadService();

    // GET：プレビュー画面表示（PDF/Text）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ログインユーザー取得
        HttpSession session = request.getSession(false);
        UserDto loginUser = (session != null) ? (UserDto) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // フォーマットと会議ID取得
        String meetingIdStr = request.getParameter("meetingId");
        String format = request.getParameter("format");

        if (meetingIdStr != null) {
            try {
                int meetingId = Integer.parseInt(meetingIdStr);
                MeetingDetailsDto meeting = downloadService.generatePreviewData(meetingId, loginUser.getUserId());

                if ("text".equalsIgnoreCase(format)) {
                    // Textプレビュー
                    String textPreview = TextDownloader.generatePreview(meeting);
                    request.setAttribute("textPreview", textPreview);
                    request.setAttribute("meetingId", meetingIdStr);
                    request.getRequestDispatcher("/WEB-INF/jsp/download/previewText.jsp").forward(request, response);
                } else {
                    // PDF風プレビュー
                    request.setAttribute("meeting", meeting);
                    request.getRequestDispatcher("/WEB-INF/jsp/download/previewPdf.jsp").forward(request, response);
                }
                return;

            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "不正な会議IDです");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "プレビュー生成中にエラーが発生しました");
                return;
            }
        }

        // 会議一覧表示（検索あり）
        String searchName = request.getParameter("searchName");
        String searchDate = request.getParameter("searchDate");

        request.setAttribute("searchName", searchName);
        request.setAttribute("searchDate", searchDate);

        try {
            List<MinutesManagementAndOutputDto> meetingList =
                    downloadService.searchMeetings(searchName, searchDate);
            request.setAttribute("meetingList", meetingList);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "会議情報の取得中にエラーが発生しました。");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/download/preview.jsp").forward(request, response);
    }

    // POST：テキスト出力処理（ダウンロード）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        UserDto loginUser = (session != null) ? (UserDto) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String meetingIdStr = request.getParameter("meetingId");

        if (meetingIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "会議IDが指定されていません。");
            return;
        }

        try {
            int meetingId = Integer.parseInt(meetingIdStr);
            MeetingDetailsDto meeting = downloadService.generatePreviewData(meetingId, loginUser.getUserId());

            // ファイル名（例：minutes_20250623.txt）
            String fileName = "minutes_" + System.currentTimeMillis() + ".txt";

            response.setContentType("text/plain; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // 出力文字列生成・書き込み
            String text = TextDownloader.generatePreview(meeting);
            response.getWriter().write(text);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "出力中にエラーが発生しました。");
        }
    }
}
