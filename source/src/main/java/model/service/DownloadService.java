package model.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.dao.MeetingDetailsDao;
import model.join.MeetingDetailsDto;
import util.download.PDFDownloader;
import util.download.TextDownloader;

/**
 * 議事録の出力関連機能を扱うサービスクラス。
 * PDF・テキスト出力やプレビュー生成に対応する。
 */
public class DownloadService {

    /**
     * DB接続取得（簡略版）
     */
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/gijirokun_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "your_password";
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * プレビュー用に議題一覧データを取得する。
     */
    public MeetingDetailsDto generatePreviewData(int meetingId) {
        try (Connection conn = getConnection()) {
            MeetingDetailsDao dao = new MeetingDetailsDao(conn);
            return dao.findByMeetingId(meetingId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * PDFとして議事録を出力する。
     */
    public boolean exportToPDF(MeetingDetailsDto meetingDetails, String filePath) {
        return PDFDownloader.download(meetingDetails, filePath);
    }

    /**
     * テキストとして議事録を出力する。
     */
    public boolean exportToText(MeetingDetailsDto meetingDetails, String filePath) {
        return TextDownloader.download(meetingDetails, filePath);
    }
}