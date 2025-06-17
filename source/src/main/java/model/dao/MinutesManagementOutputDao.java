package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dto.MinutesManagementAndOutputDto;

/**
 * 会議情報取得用DAO（検索画面用）
 */
public class MinutesManagementOutputDao {
    private final Connection conn;

    public MinutesManagementOutputDao(Connection conn) {
        this.conn = conn;
    }

    public List<MinutesManagementAndOutputDto> searchMeetings(String name, String date) throws SQLException {
        List<MinutesManagementAndOutputDto> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT meeting_id, title, meeting_date ");
        sql.append("FROM meetings ");
        sql.append("WHERE is_deleted = 0 ");

        // 可変条件の構築
        List<Object> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            sql.append("AND title LIKE ? ");
            params.add("%" + name + "%");
        }
        if (date != null && !date.isEmpty()) {
            sql.append("AND meeting_date = ? ");
            params.add(date);
        }

        sql.append("ORDER BY meeting_date DESC");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MinutesManagementAndOutputDto dto = new MinutesManagementAndOutputDto();
                    dto.setmeeting_id(rs.getInt("meeting_id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setMeetingDate(rs.getDate("meeting_date"));

                    list.add(dto);
                }
            }
        }

        return list;
    }
}
