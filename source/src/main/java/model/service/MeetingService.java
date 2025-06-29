package model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.dao.AgendaDao;
import model.dao.MeetingDao;
import model.dao.MinutesManagementOutputDao;
import model.dto.AgendaDto;
import model.dto.MeetingDto;
import model.dto.MinutesManagementAndOutputDto;
import util.Validator;

/**
 * 会議の管理を行うサービスクラス
 */
public class MeetingService extends BaseService {

    // ===== 会議一覧取得（検索・ソート機能付き） =====
    public List<MeetingDto> findAllMeetings(String meetingName, String meetingDate, String sort) throws Exception {
        MeetingDao meetingDao = new MeetingDao();
        List<MeetingDto> meetings = meetingDao.findAll();

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

    // ===== 会議詳細を取得 =====
    public MeetingDto findMeetingById(int meetingId) throws Exception {
        MeetingDao meetingDao = new MeetingDao();
        return meetingDao.findById(meetingId);
    }

    // ===== 会議の更新 =====
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

    // ===== 会議の論理削除 =====
    public void deleteMeeting(int meetingId) throws Exception {
        MeetingDao meetingDao = new MeetingDao();
        boolean success = meetingDao.delete(meetingId);
        if (!success) {
            throw new Exception("会議の削除に失敗しました");
        }
    }

    // ===== 会議の新規作成 =====
    public int createMeeting(MeetingDto dto) throws Exception {
        if (Validator.isEmpty(dto.getTitle())) {
            throw new IllegalArgumentException("会議タイトルは必須です");
        }

        try (Connection conn = getConnection()) {
            MeetingDao meetingDao = new MeetingDao();
            
            // ✅ タイトル＋日付の重複チェック
            List<MeetingDto> existing = meetingDao.findAll().stream()
                .filter(m -> m.getTitle().equals(dto.getTitle())
                          && m.getMeetingDate().equals(dto.getMeetingDate()))
                .collect(Collectors.toList());

            if (!existing.isEmpty()) {
                throw new IllegalArgumentException("同じタイトルと日付の会議がすでに存在します。");
            }
            
            return meetingDao.insert(dto, conn);
        }
    }

    public void addAgenda(AgendaDto agenda) throws Exception {
        if (Validator.isEmpty(agenda.getTitle())) {
            throw new IllegalArgumentException("議題名は必須です");
        }

        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.insertAgenda(agenda);
        }
    }
    
    public void updateAgenda(AgendaDto agenda) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.updateAgenda(agenda);
        }
    }
    
    public void deleteAgenda(int agendaId) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            dao.logicalDelete(agendaId);
        }
    }
    
    public List<AgendaDto> getAgendasByMeetingId(int meetingId) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            return dao.findByMeetingId(meetingId);
        }
    }
    
    // ===== プレビュー確認後の議事録確定保存 =====
    public void finalizeMeeting(int meetingId, int userId) throws Exception {
        try (Connection conn = getConnection()) {
            MinutesManagementOutputDao dao = new MinutesManagementOutputDao(conn);

            // MinutesManagementAndOutputDtoを作成してログを記録
            MinutesManagementAndOutputDto dto = new MinutesManagementAndOutputDto();
            dto.setmeeting_id(meetingId);
            dto.setCreatedBy(userId);
            dto.setOutputFormat("preview"); // 保存形式はまだ未選択なので仮で "preview" を指定
            
            dao.insertOutputLog(dto);
        }
    }

    public void addAgendaList(List<AgendaDto> agendaList) throws Exception {
        if (agendaList == null || agendaList.isEmpty()) {
            throw new IllegalArgumentException("議題リストが空です");
        }

        // タイトルが空の議題が含まれていないか確認（必須チェック）
        for (AgendaDto agenda : agendaList) {
            if (Validator.isEmpty(agenda.getTitle())) {
                throw new IllegalArgumentException("すべての議題にはタイトルが必要です");
            }
        }

        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            for (AgendaDto agenda : agendaList) {
                dao.insertAgenda(agenda);
            }
        }
    }
    
    // ===== 編集画面で使う：議題リストの取得 =====
    public List<AgendaDto> findAgendasByMeetingId(int meetingId) throws Exception {
        try (Connection conn = getConnection()) {
            AgendaDao dao = new AgendaDao(conn);
            return dao.findByMeetingId(meetingId);
        }
    }

    // ===== 会議＋議題の編集保存（update or insert 混在対応） =====
    public void updateMeetingAndAgendasSmart(MeetingDto meeting, List<AgendaDto> submittedAgendas) throws Exception {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                // 1. 会議情報を更新
                MeetingDao meetingDao = new MeetingDao();
                if (!meetingDao.update(meeting, conn)) {
                    throw new SQLException("会議の更新に失敗しました: ID=" + meeting.getMeetingId());
                }

                AgendaDao agendaDao = new AgendaDao(conn);
                
                // 2. DBに保存されている既存の議題IDリストを取得
                List<AgendaDto> dbAgendas = agendaDao.findByMeetingId(meeting.getMeetingId());
                List<Integer> dbAgendaIds = new ArrayList<>();
                for (AgendaDto a : dbAgendas) {
                    dbAgendaIds.add(a.getAgendaId());
                }

                // 3. フォームから送信された議題IDリストを作成
                List<Integer> submittedAgendaIds = new ArrayList<>();
                for (AgendaDto a : submittedAgendas) {
                    if (a.getAgendaId() != 0) {
                        submittedAgendaIds.add(a.getAgendaId());
                    }
                }

                // 4. 削除された議題を特定して論理削除
                // (DBにはあるが、フォームにはない)
                for (Integer dbId : dbAgendaIds) {
                    if (!submittedAgendaIds.contains(dbId)) {
                        agendaDao.logicalDelete(dbId);
                    }
                }

                // 5. 送信された議題を更新または新規登録
                for (AgendaDto submittedAgenda : submittedAgendas) {
                    if (submittedAgenda.getAgendaId() != 0) {
                        // 既存の議題 -> 更新
                        agendaDao.updateAgenda(submittedAgenda);
                    } else {
                        // 新規議題 -> 登録
                        agendaDao.insertAgenda(submittedAgenda);
                    }
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new Exception("データベース保存処理中にエラーが発生しました。", e);
            }
        } catch (Exception e) {
            // エラーログは詳細に出力
            System.err.println("会議と議題の保存に失敗: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("保存処理中に予期せぬエラーが発生しました。", e);
        }
    }
}