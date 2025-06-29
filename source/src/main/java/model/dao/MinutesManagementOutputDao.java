package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.dto.AgendaDto;
import model.dto.MeetingDetailsDto;
import model.dto.MinutesManagementAndOutputDto;

/**
 * 会議情報取得用DAO（検索・出力画面用）
 */
public class MinutesManagementOutputDao {
	private final Connection conn;

	public MinutesManagementOutputDao(Connection conn) {
		this.conn = conn;
	}

	// ▼ 検索画面の会議一覧取得 ▼
	public List<MinutesManagementAndOutputDto> searchMeetings(String name, String date) throws SQLException {
		List<MinutesManagementAndOutputDto> list = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT meeting_id, title, meeting_date ");
		sql.append("FROM meetings ");
		sql.append("WHERE is_deleted = 0 ");

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

	// ▼ 議事録テキスト出力用 ▼
	public MinutesManagementAndOutputDto findMeetingDetailsById(int meetingId) throws SQLException {
		MinutesManagementAndOutputDto dto = new MinutesManagementAndOutputDto();

		// 会議情報取得
		String meetingSql = "SELECT title, meeting_date, start_time, end_time, participants_text FROM meetings WHERE meeting_id = ? AND is_deleted = 0";
		try (PreparedStatement stmt = conn.prepareStatement(meetingSql)) {
			stmt.setInt(1, meetingId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					dto.setmeeting_id(meetingId);
					dto.setTitle(rs.getString("title"));
					dto.setMeetingDate(rs.getDate("meeting_date"));
					dto.setStartTime(rs.getTime("start_time"));
					dto.setEndTime(rs.getTime("end_time"));

					String participantsText = rs.getString("participants_text");
					if (participantsText != null && !participantsText.isEmpty()) {
						String[] names = participantsText.split("\\s*,\\s*");
						dto.setParticipants(Arrays.asList(names));
					} else {
						dto.setParticipants(new ArrayList<>());
					}
				} else {
					return null;
				}
			}
		}

		// 議題取得
		String agendaSql = "SELECT title, speech_note, decision_note FROM agendas WHERE meeting_id = ? AND is_deleted = 0 ORDER BY order_number ASC";
		try (PreparedStatement stmt = conn.prepareStatement(agendaSql)) {
			stmt.setInt(1, meetingId);
			try (ResultSet rs = stmt.executeQuery()) {
				List<AgendaDto> agendas = new ArrayList<>();
				while (rs.next()) {
					AgendaDto agenda = new AgendaDto();
					agenda.setTitle(rs.getString("title"));
					agenda.setSpeechNote(rs.getString("speech_note"));
					agenda.setDecisionNote(rs.getString("decision_note"));
					agendas.add(agenda);
				}
				dto.setAgendas(agendas);
			}
		}

		return dto;
	}

	// ▼ PDF出力用 MeetingDetailsDto 取得メソッド ▼
	public MeetingDetailsDto findMeetingDetailsDtoById(int meetingId) throws SQLException {
		MeetingDetailsDto dto = new MeetingDetailsDto();

		// 会議情報取得
		String sql = "SELECT title, meeting_date, start_time, end_time, participants_text FROM meetings WHERE meeting_id = ? AND is_deleted = 0";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, meetingId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					dto.setMeetingId(meetingId);
					dto.setTitle(rs.getString("title"));
					dto.setMeetingDate(rs.getDate("meeting_date").toLocalDate());
					dto.setStartTime(rs.getTime("start_time").toLocalTime());
					dto.setEndTime(rs.getTime("end_time").toLocalTime());
					dto.setParticipantsText(rs.getString("participants_text"));
				} else {
					return null;
				}
			}
		}

		// 議題取得
		String agendaSql = "SELECT title, speech_note, decision_note FROM agendas WHERE meeting_id = ? AND is_deleted = 0 ORDER BY order_number ASC";
		try (PreparedStatement stmt = conn.prepareStatement(agendaSql)) {
			stmt.setInt(1, meetingId);
			try (ResultSet rs = stmt.executeQuery()) {
				List<AgendaDto> agendas = new ArrayList<>();
				while (rs.next()) {
					AgendaDto agenda = new AgendaDto();
					agenda.setTitle(rs.getString("title"));
					agenda.setSpeechNote(rs.getString("speech_note"));
					agenda.setDecisionNote(rs.getString("decision_note"));
					agendas.add(agenda);
				}
				dto.setAgendas(agendas);
			}
		}

		return dto;
	}

	// ▼ 出力ログ記録 ▼
	public void insertOutputLog(MinutesManagementAndOutputDto dto) throws SQLException {
		String sql = "INSERT INTO MinutesManagementOutput (meeting_id, created_by, output_format) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, dto.getmeeting_id());
			ps.setInt(2, dto.getCreatedBy());
			ps.setString(3, dto.getOutputFormat());
			ps.executeUpdate();
		}
	}
}
