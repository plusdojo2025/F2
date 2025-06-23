package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dto.MeetingDto;

public class MeetingDao {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ JDBCドライバ読み込み成功");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBCドライバ読み込み失敗: " + e.getMessage());
        }
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/f2?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    // ===== 全件取得 =====
    public List<MeetingDto> findAll() {
        List<MeetingDto> meetings = new ArrayList<>();
        String sql = "SELECT * FROM meetings WHERE is_deleted = 0 ORDER BY meeting_id ASC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                meetings.add(toDto(rs));
            }
            System.out.println("📝 会議取得件数: " + meetings.size());

        } catch (Exception e) {
            System.err.println("❌ findAllエラー: " + e.getMessage());
            e.printStackTrace();
        }

        return meetings;
    }

    // ===== IDで1件取得 =====
    public MeetingDto findById(int meetingId) {
        String sql = "SELECT * FROM meetings WHERE meeting_id = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, meetingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("🔍 会議取得 ID=" + meetingId);
                    return toDto(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ findByIdエラー: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // ===== 登録（Connectionなし） =====
    public int insert(MeetingDto meeting) {
        int generatedId = -1;
        String sql = "INSERT INTO meetings (title, meeting_date, start_time, end_time, participants_text, created_by, is_deleted, created_at, updated_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, 0, NOW(), NOW())";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, meeting.getTitle());
            ps.setDate(2, meeting.getMeetingDate());
            ps.setTime(3, meeting.getStartTime());
            ps.setTime(4, meeting.getEndTime());
            ps.setString(5, meeting.getParticipantsText());
            ps.setInt(6, meeting.getCreatedBy());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        System.out.println("✅ 会議登録成功 ID=" + generatedId);
                    }
                }
            } else {
                System.err.println("⚠️ 会議登録失敗（0件挿入）");
            }

        } catch (Exception e) {
            System.err.println("❌ insertエラー: " + e.getMessage());
            e.printStackTrace();
        }

        return generatedId;
    }

    // ===== 登録（Connectionあり） =====
    public int insert(MeetingDto meeting, Connection conn) {
        int generatedId = -1;
        String sql = "INSERT INTO meetings (title, meeting_date, start_time, end_time, participants_text, created_by, is_deleted, created_at, updated_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, 0, NOW(), NOW())";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, meeting.getTitle());
            ps.setDate(2, meeting.getMeetingDate());
            ps.setTime(3, meeting.getStartTime());
            ps.setTime(4, meeting.getEndTime());
            ps.setString(5, meeting.getParticipantsText());
            ps.setInt(6, meeting.getCreatedBy());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        System.out.println("✅ 会議登録成功 ID=" + generatedId);
                    }
                }
            } else {
                System.err.println("⚠️ 会議登録失敗（0件挿入）");
            }

        } catch (Exception e) {
            System.err.println("❌ insertエラー: " + e.getMessage());
            e.printStackTrace();
        }

        return generatedId;
    }

    // ===== 更新（Connectionなし） =====
    public boolean update(MeetingDto meeting) {
        String sql = "UPDATE meetings SET title=?, meeting_date=?, start_time=?, end_time=?, "
                   + "participants_text=?, detail_area=?, decisions=?, updated_at=NOW() WHERE meeting_id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, meeting.getTitle());
            ps.setDate(2, meeting.getMeetingDate());
            ps.setTime(3, meeting.getStartTime());
            ps.setTime(4, meeting.getEndTime());
            ps.setString(5, meeting.getParticipantsText());
            ps.setString(6, meeting.getDetailArea());
            ps.setString(7, meeting.getDecisions());
            ps.setInt(8, meeting.getMeetingId());

            int rows = ps.executeUpdate();
            System.out.println("🔄 会議更新 ID=" + meeting.getMeetingId() + " 件数=" + rows);
            return rows > 0;

        } catch (Exception e) {
            System.err.println("❌ updateエラー: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // ===== 更新（Connectionあり） =====
    public boolean update(MeetingDto meeting, Connection conn) {
        String sql = "UPDATE meetings SET title=?, meeting_date=?, start_time=?, end_time=?, "
                   + "participants_text=?, detail_area=?, decisions=?, updated_at=NOW() WHERE meeting_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, meeting.getTitle());
            ps.setDate(2, meeting.getMeetingDate());
            ps.setTime(3, meeting.getStartTime());
            ps.setTime(4, meeting.getEndTime());
            ps.setString(5, meeting.getParticipantsText());
            ps.setString(6, meeting.getDetailArea());
            ps.setString(7, meeting.getDecisions());
            ps.setInt(8, meeting.getMeetingId());

            int rows = ps.executeUpdate();
            System.out.println("🔄 会議更新 ID=" + meeting.getMeetingId() + " 件数=" + rows);
            return rows > 0;

        } catch (Exception e) {
            System.err.println("❌ updateエラー: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // ===== 論理削除 =====
    public boolean delete(int meetingId) {
        String sql = "UPDATE meetings SET is_deleted=1, updated_at=NOW() WHERE meeting_id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, meetingId);
            int rows = ps.executeUpdate();
            System.out.println("🗑 会議削除 ID=" + meetingId + " 件数=" + rows);
            return rows > 0;

        } catch (Exception e) {
            System.err.println("❌ deleteエラー: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // ===== DTO変換 =====
    private MeetingDto toDto(ResultSet rs) throws SQLException {
        return new MeetingDto(
            rs.getInt("meeting_id"),
            rs.getString("title"),
            rs.getDate("meeting_date"),
            rs.getTime("start_time"),
            rs.getTime("end_time"),
            rs.getInt("created_by"),
            rs.getString("participants_text"),
            rs.getBoolean("is_deleted"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at"),
            rs.getString("detail_area"),
            rs.getString("decisions")
        );
    }

    // ===== Connection取得（必要な場合） =====
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            System.err.println("❌ getConnectionエラー: " + e.getMessage());
            return null;
        }
    }
}