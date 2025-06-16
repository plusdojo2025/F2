package model.service;

import java.time.LocalDate;
//import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

//import model.dao.AgendaDao;
import model.dao.MeetingDao;
//import model.dto.AgendaDto;
import model.dto.MeetingDto;
//import model.join.MeetingDetailsDto;
import util.Validator;

/**
 * 会議の管理を行うサービスクラス
 */
public class MeetingService {  // BaseServiceの継承を削除

    // ===== 実装済みの機能（会議の基本操作） =====

	 /**
     * 会議一覧取得（検索・ソート機能付き）
     */
    public List<MeetingDto> findAllMeetings(String meetingName, String meetingDate, String sort) throws Exception {
        MeetingDao meetingDao = new MeetingDao();
        List<MeetingDto> meetings = meetingDao.findAll();
        
        // 検索条件でフィルタリング
        if (meetingName != null && !meetingName.isEmpty()) {
            meetings = meetings.stream()
                .filter(m -> m.getTitle().contains(meetingName))
                .collect(Collectors.toList());
        }
        
        if (meetingDate != null && !meetingDate.isEmpty()) {
            LocalDate searchDate = LocalDate.parse(meetingDate);
            meetings = meetings.stream()
                .filter(m -> m.getMeetingDate().toLocalDate().equals(searchDate))
                .collect(Collectors.toList());
        }
        
        // ソート
        if (sort != null) {
            switch (sort) {
                case "dateDesc":
                    meetings.sort((a, b) -> b.getMeetingDate().compareTo(a.getMeetingDate()));
                    break;
                case "dateAsc":
                    meetings.sort((a, b) -> a.getMeetingDate().compareTo(b.getMeetingDate()));
                    break;
                case "nameAsc":
                    meetings.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
                    break;
                case "nameDesc":
                    meetings.sort((a, b) -> b.getTitle().compareTo(a.getTitle()));
                    break;
            }
        }
        
        return meetings;
    }

    /**
     * 会議詳細を取得
     */
    public MeetingDto findMeetingById(int meetingId) throws Exception {
        MeetingDao meetingDao = new MeetingDao();
        return meetingDao.findById(meetingId);
    }

    /**
     * 会議の更新
     */
    public void updateMeeting(MeetingDto dto) throws Exception {
        if (Validator.isEmpty(dto.getTitle())) {
            throw new IllegalArgumentException("会議タイトルは必須です");
        }

        MeetingDao meetingDao = new MeetingDao();
        boolean success = meetingDao.update(dto);
        if (!success) {
            throw new Exception("会議の更新に失敗しました");
        }
    }

    /**
     * 会議の論理削除
     */
    public void deleteMeeting(int meetingId) throws Exception {
        MeetingDao meetingDao = new MeetingDao();
        boolean success = meetingDao.delete(meetingId);
        if (!success) {
            throw new Exception("会議の削除に失敗しました");
        }
    }

    // ===== 未実装の機能（会議の作成と議題関連） =====

    /**
     * 会議の新規作成（未実装）
     */
    /*
    public void createMeeting(MeetingDto dto) throws Exception {
        if (Validator.isEmpty(dto.getTitle())) {
            throw new IllegalArgumentException("会議タイトルは必須です");
        }

        Connection conn = null;
        try {
            conn = getConnection();
            MeetingDao meetingDao = new MeetingDao(conn);
            meetingDao.insert(dto);
        } finally {
            closeQuietly(conn);
        }
    }
    */

    /**
     * 議題を追加する（未実装）
     */
    /*
    public void addAgenda(AgendaDto agenda) throws Exception {
        if (Validator.isEmpty(agenda.getTitle())) {
            throw new IllegalArgumentException("議題名は必須です");
        }

        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.insertAgenda(agenda);
        }
    }
    */

    /**
     * 議題を更新する（未実装）
     */
    /*
    public void updateAgenda(AgendaDto agenda) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.updateAgenda(agenda);
        }
    }
    */

    /**
     * 議題を削除する（未実装）
     */
    /*
    public void deleteAgenda(int agendaId) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.logicalDelete(agendaId);
        }
    }
    */

    /**
     * 議題一覧を取得（未実装）
     */
    /*
    public List<AgendaDto> getAgendasByMeetingId(int meetingId) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            return dao.findByMeetingId(meetingId);
        }
    }
    */
}