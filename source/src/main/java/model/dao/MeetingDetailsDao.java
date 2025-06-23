package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.AgendaDto;
import model.dto.MeetingDetailsDto;

/**
 * 会議詳細（会議情報＋議題リスト）を取得するDAO。
 */
public class MeetingDetailsDao {

    /**
     * 指定した meeting_id の会議情報と、それに紐づく議題一覧を取得する。
     */
    public MeetingDetailsDto findMeetingWithAgendas(Connection conn, int meetingId) throws Exception {
        MeetingDetailsDto meeting = null;

        // 1. 会議情報を取得
        String meetingSql = "SELECT * FROM meetings WHERE meeting_id = ? AND is_deleted = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(meetingSql)) {
            pstmt.setInt(1, meetingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    meeting = new MeetingDetailsDto();
                    meeting.setMeetingId(rs.getInt("meeting_id"));
                    meeting.setTitle(rs.getString("title"));
                    meeting.setMeetingDate(rs.getDate("meeting_date").toLocalDate());
                    meeting.setStartTime(rs.getTime("start_time").toLocalTime());
                    meeting.setEndTime(rs.getTime("end_time").toLocalTime());
                    meeting.setParticipantsText(rs.getString("participants_text"));
                    meeting.setDetailArea(rs.getString("detail_area"));
                    meeting.setDecisions(rs.getString("decisions"));
                }
            }
        }

        if (meeting == null) return null;

        // 2. 議題一覧を取得
        String agendaSql = "SELECT * FROM agendas WHERE meeting_id = ? AND is_deleted = 0 ORDER BY order_number";
        try (PreparedStatement pstmt = conn.prepareStatement(agendaSql)) {
            pstmt.setInt(1, meetingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<AgendaDto> agendaList = new ArrayList<>();
                while (rs.next()) {
                    AgendaDto agenda = new AgendaDto();
                    agenda.setAgendaId(rs.getInt("agenda_id"));
                    agenda.setMeetingId(rs.getInt("meeting_id"));
                    agenda.setTitle(rs.getString("title"));
                    agenda.setDescription(rs.getString("description"));
                    agenda.setOrderNumber(rs.getInt("order_number"));
                    agenda.setSpeechNote(rs.getString("speech_note"));
                    agenda.setDecisionNote(rs.getString("decision_note"));
                    agendaList.add(agenda);
                }
                meeting.setAgendas(agendaList);
            }
        }

        return meeting;
    }
}

