package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.MeetingDto;

public class MeetingDao {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL JDBC Driver: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/f2?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public List<MeetingDto> findAll() {
        List<MeetingDto> meetings = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Database connection successful");
            String sql = "SELECT * FROM meetings WHERE is_deleted = 0 ORDER BY meeting_id ASC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MeetingDto dto = new MeetingDto(
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
                    meetings.add(dto);
                }
                System.out.println("Found " + meetings.size() + " meetings");
            }
        } catch (Exception e) {
            System.err.println("Error in findAll(): " + e.getMessage());
            e.printStackTrace();
        }
        return meetings;
    }

    public MeetingDto findById(int meetingId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM meetings WHERE meeting_id = ? AND is_deleted = 0";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, meetingId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        MeetingDto dto = new MeetingDto(
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
                        return dto;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(MeetingDto meeting) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE meetings SET title=?, meeting_date=?, start_time=?, end_time=?, participants_text=?, detail_area=?, decisions=? WHERE meeting_id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, meeting.getTitle());
                ps.setDate(2, meeting.getMeetingDate());
                ps.setTime(3, meeting.getStartTime());
                ps.setTime(4, meeting.getEndTime());
                ps.setString(5, meeting.getParticipantsText());
                ps.setString(6, meeting.getDetailArea());
                ps.setString(7, meeting.getDecisions());
                ps.setInt(8, meeting.getMeetingId());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int meetingId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE meetings SET is_deleted=1 WHERE meeting_id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, meetingId);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}