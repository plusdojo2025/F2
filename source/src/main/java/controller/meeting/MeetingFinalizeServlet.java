package controller.meeting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dto.UserDto;
import model.service.MeetingService;

/**
 * プレビュー確認後に議事録保存（確定）処理を行うサーブレット。
 */
@WebServlet("/meeting/finalize")
public class MeetingFinalizeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // セッションからログインユーザー取得
            HttpSession session = request.getSession(false);
            UserDto loginUser = (session != null) ? (UserDto) session.getAttribute("loginUser") : null;

            if (loginUser == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // パラメータ取得
            String meetingIdParam = request.getParameter("meetingId");

            if (meetingIdParam == null || meetingIdParam.isEmpty()) {
                request.setAttribute("error", "会議IDが指定されていません。");
                request.getRequestDispatcher("/WEB-INF/jsp/download/error.jsp").forward(request, response);
                return;
            }

            int meetingId = Integer.parseInt(meetingIdParam);
            int userId = loginUser.getUserId();

            // 議事録を保存（フォーマット指定なしで仮登録）
            MeetingService service = new MeetingService();
            service.finalizeMeeting(meetingId, userId);

            // 保存成功後 → ダウンロード画面へ遷移
            response.sendRedirect(request.getContextPath() + "/download");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "保存中にエラーが発生しました。");
            request.getRequestDispatcher("/WEB-INF/jsp/download/error.jsp").forward(request, response);
        }
    }
} 