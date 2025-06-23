package model.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import model.dao.MeetingDetailsDao;
import model.dao.MinutesManagementOutputDao;
import model.dto.MeetingDetailsDto;
import model.dto.MinutesManagementAndOutputDto;
import util.download.PDFDownloader;
import util.download.TextDownloader;

/**
 * 議事録の出力関連機能を扱うサービスクラス。
 * PDF・テキスト出力、プレビュー生成、出力ログ保存に対応。
 */
public class DownloadService {

    /**
     * DB接続を取得する（f2データベース用）
     */
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/f2"
                   + "?useSSL=false"
                   + "&characterEncoding=utf8"
                   + "&serverTimezone=Asia/Tokyo";
        String user = "root";
        String password = "password";
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * プレビュー用に会議情報（議題含む）を取得する。
     * ログインユーザーIDをDTOにセットして返却する。
     */
    public MeetingDetailsDto generatePreviewData(int meetingId, int loginUserId) {
        try (Connection conn = getConnection()) {
            MeetingDetailsDao dao = new MeetingDetailsDao();
            MeetingDetailsDto dto = dao.findMeetingWithAgendas(conn, meetingId);
            if (dto != null) {
                dto.setCreatedBy(loginUserId);
            }
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 会議の検索（出力画面の一覧取得用） ※会議IDで重複排除済み
     */
    public List<MinutesManagementAndOutputDto> searchMeetings(String searchName, String searchDate) {
        try (Connection conn = getConnection()) {
            MinutesManagementOutputDao dao = new MinutesManagementOutputDao(conn);
            List<MinutesManagementAndOutputDto> rawList = dao.searchMeetings(searchName, searchDate);

            // 重複排除：meetingId でグルーピングして先頭要素だけを残す
            Map<Integer, MinutesManagementAndOutputDto> uniqueMap =
                rawList.stream()
                       .collect(Collectors.toMap(
                           MinutesManagementAndOutputDto::getmeeting_id,
                           dto -> dto,
                           (existing, replacement) -> existing // 重複は無視して1件にする
                       ));

            return uniqueMap.values().stream().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * PDFとして議事録を出力する（フォントパスを外部から渡す）。
     */
    public boolean exportToPDF(MeetingDetailsDto meetingDetails, String filePath, ServletContext context) {
        String fontPath = context.getRealPath("/assets/font/NotoSansJP-Regular.ttf");
        boolean success = PDFDownloader.download(meetingDetails, filePath, fontPath);
        if (success) {
            logOutputHistory(meetingDetails.getMeetingId(), meetingDetails.getCreatedBy(), "pdf");
        }
        return success;
    }

    /**
     * テキストとして議事録を出力する。
     */
    public boolean exportToText(MeetingDetailsDto meetingDetails, String filePath) {
        boolean success = TextDownloader.download(meetingDetails, filePath);
        if (success) {
            logOutputHistory(meetingDetails.getMeetingId(), meetingDetails.getCreatedBy(), "text");
        }
        return success;
    }

    /**
     * 出力ログをDBに記録する（PDF or Text）
     */
    private void logOutputHistory(int meetingId, int userId, String format) {
        try (Connection conn = getConnection()) {
            MinutesManagementOutputDao dao = new MinutesManagementOutputDao(conn);
            MinutesManagementAndOutputDto dto = new MinutesManagementAndOutputDto();
            dto.setmeeting_id(meetingId);
            dto.setCreatedBy(userId);
            dto.setOutputFormat(format);
            dao.insertOutputLog(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
