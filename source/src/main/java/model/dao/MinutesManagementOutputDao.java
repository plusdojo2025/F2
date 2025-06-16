package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.MinutesManagementAndOutputDto;

public class MinutesManagementOutputDao {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gijirokun?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    /**
     * 会議名の部分一致・会議開催日で議事録出力対象を検索するメソッド
     *
     * @param titlePart 会議名の部分文字列（nullや空文字なら条件なし）
     * @param meetingDate 会議開催日（nullなら条件なし）
     * @return 検索結果のDTOリスト
     */
    public List<MinutesManagementAndOutputDto> searchByTitleAndDate(String titlePart, java.sql.Date meetingDate) {
        List<MinutesManagementAndOutputDto> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT mmo.minute_id, mmo.meeting_id, m.title, m.meeting_date, ");
        sql.append("mmo.output_format, mmo.created_at ");
        sql.append("FROM MinutesManagementOutput mmo ");
        sql.append("JOIN meetings m ON mmo.meeting_id = m.meeting_id ");
        sql.append("WHERE m.is_deleted = 0 ");

        // パラメータ追加用のリスト
        List<Object> params = new ArrayList<>();

        if (titlePart != null && !titlePart.isEmpty()) {
            sql.append("AND m.title LIKE ? ");
            params.add("%" + titlePart + "%");
        }

        if (meetingDate != null) {
            sql.append("AND m.meeting_date = ? ");
            params.add(meetingDate);
        }

        sql.append("ORDER BY m.meeting_date DESC, mmo.created_at DESC");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MinutesManagementAndOutputDto dto = new MinutesManagementAndOutputDto();
                    dto.setMinuteId(rs.getInt("minute_id"));
                    dto.setMeetingId(rs.getInt("meeting_id"));
                    dto.setTitle(rs.getString("title")); // DTOにtitleフィールドが必要
                    dto.setMeetingDate(rs.getDate("meeting_date")); // DTOにmeetingDateフィールドが必要
                    dto.setOutputFormat(rs.getString("output_format"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));

                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
