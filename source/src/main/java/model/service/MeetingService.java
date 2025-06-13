package model.service;

import java.sql.Connection;
import java.util.List;

import model.dao.AgendaDao;
import model.dao.MeetingDao;
import model.dto.AgendaDto;
import model.dto.MeetingDto;
import model.join.MeetingDetailsDto;
import util.Validator;

/**
 * 会議・議題・発言・決定事項の管理を行うサービスクラス
 */
public class MeetingService extends BaseService {

    /**
     * 会議の新規作成
     */
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

    /**
     * 会議一覧取得（削除済を除く）
     */
    public List<MeetingDto> findAllMeetings(int userId) throws Exception {
        try (Connection conn = getConnection()) {
            MeetingDao dao = new MeetingDao(conn);
            return dao.findByUserId(userId);
        }
    }

    /**
     * 会議詳細 + 議題情報を取得
     */
    public MeetingDetailsDto findMeetingDetails(int meetingId) throws Exception {
        try (Connection conn = getConnection()) {
            return new MeetingDao(conn).findDetailsById(meetingId);
        }
    }

    /**
     * 会議の更新
     */
    public void updateMeeting(MeetingDto dto) throws Exception {
        if (Validator.isEmpty(dto.getTitle())) {
            throw new IllegalArgumentException("会議タイトルは必須です");
        }

        try (Connection conn = getConnection()) {
            MeetingDao dao = new MeetingDao(conn);
            dao.update(dto);
        }
    }

    /**
     * 会議の論理削除
     */
    public void deleteMeeting(int meetingId) throws Exception {
        try (Connection conn = getConnection()) {
            MeetingDao dao = new MeetingDao(conn);
            dao.logicalDelete(meetingId);
        }
    }

    /**
     * 議題を追加する
     */
    public void addAgenda(AgendaDto agenda) throws Exception {
        if (Validator.isEmpty(agenda.getTitle())) {
            throw new IllegalArgumentException("議題名は必須です");
        }

        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.insert(agenda);
        }
    }

    /**
     * 議題を更新する（発言・決定事項を含む）
     */
    public void updateAgenda(AgendaDto agenda) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.update(agenda);
        }
    }

    /**
     * 議題を削除する（論理削除）
     */
    public void deleteAgenda(int agendaId) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.logicalDelete(agendaId);
        }
    }

    /**
     * 議題一覧を取得
     */
    public List<AgendaDto> getAgendasByMeetingId(int meetingId) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            return dao.findByMeetingId(meetingId);
        }
    }
}