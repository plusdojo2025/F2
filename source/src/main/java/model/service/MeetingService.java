package model.service;
import java.sql.Connection;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import model.dao.AgendaDao;
import model.dao.MeetingDao;
import model.dto.AgendaDto;
import model.dto.MeetingDto;
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
    // ===== 会議の新規作成（時間はここで整形）=====
    public int createMeeting(MeetingDto dto, String timeInput) throws Exception {
        if (Validator.isEmpty(dto.getTitle())) {
            throw new IllegalArgumentException("会議タイトルは必須です");
        }
        if (Validator.isEmpty(timeInput)) {
            throw new IllegalArgumentException("時間の入力は必須です");
        }
        // 全角「〜」を半角「~」に統一
        timeInput = timeInput.replace("〜", "~");
        if (!timeInput.contains("~")) {
            throw new IllegalArgumentException("時間の形式が不正です。例：10:00〜11:00 または 10:00~11:00");
        }
        try {
            String[] timeParts = timeInput.split("~");
            if (timeParts.length != 2) {
                throw new IllegalArgumentException("時間の入力は「開始~終了」の形式にしてください。");
            }
            String startTimeStr = timeParts[0].trim();
            String endTimeStr = timeParts[1].trim();
            // 秒がなければ補完（例：10:00 → 10:00:00）
            if (startTimeStr.length() == 5) startTimeStr += ":00";
            if (endTimeStr.length() == 5) endTimeStr += ":00";
            dto.setStartTime(Time.valueOf(startTimeStr));
            dto.setEndTime(Time.valueOf(endTimeStr));
        } catch (Exception e) {
            throw new IllegalArgumentException("時間の変換に失敗しました。形式を確認してください。");
        }
        MeetingDao meetingDao = new MeetingDao();
        return meetingDao.insert(dto);
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
}