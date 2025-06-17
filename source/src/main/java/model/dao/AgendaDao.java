package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dto.AgendaDto;

public class AgendaDao {
	private Connection conn;

	public AgendaDao(Connection conn) {
		this.conn = conn;
	}

	//追加
	public void insertAgenda(AgendaDto agenda) throws SQLException {
		String sql = "INSERT INTO agendas (meeting_id, title, order_number, speech_note, decision_note, is_deleted) VALUES (?, ?, ?, ?, ?, 0)";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, agenda.getMeetingId());
			stmt.setString(2, agenda.getTitle());
			stmt.setInt(3, agenda.getOrderNumber());
			stmt.setString(4, agenda.getSpeechNote());
			stmt.setString(5, agenda.getDecisionNote());
			stmt.executeUpdate();
		}
	}
	
	//更新
	public void updateAgenda(AgendaDto agenda) throws SQLException {
		String sql = "UPDATE agendas SET  title=?, order_number=?, speech_note=?, decision_note=? WHERE agenda_id = ? AND is_deleted = 0";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, agenda.getTitle());
			stmt.setInt(2, agenda.getOrderNumber());
			stmt.setString(3, agenda.getSpeechNote());
			stmt.setString(4, agenda.getDecisionNote());
            stmt.setInt(5, agenda.getAgendaId());
            stmt.executeUpdate();
        }
	}
	
	// 論理削除
    public void logicalDelete(int agendaId) throws SQLException {
        String sql = "UPDATE agendas SET is_deleted = 1 WHERE agenda_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agendaId);
            stmt.executeUpdate();
        }
    }
    
    // 会議に紐づく議題取得（削除されていないもの）
    public List<AgendaDto> findByMeetingId(int meetingId) throws SQLException {
        String sql = "SELECT * FROM agendas WHERE meeting_id = ? AND is_deleted = 0 ORDER BY order_number ASC";
        List<AgendaDto> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, meetingId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AgendaDto dto = new AgendaDto();
                    dto.setAgendaId(rs.getInt("agenda_id"));
                    dto.setMeetingId(rs.getInt("meeting_id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setDescription(rs.getString("description"));
                    dto.setOrderNumber(rs.getInt("order_number"));
                    dto.setSpeechNote(rs.getString("speech_note"));
                    dto.setDecisionNote(rs.getString("decision_note"));
                    dto.setDeleted(rs.getBoolean("is_deleted"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    dto.setUpdatedAt(rs.getTimestamp("updated_at"));
                    list.add(dto);
                }
            }
        }
        return list;
    }
	
}