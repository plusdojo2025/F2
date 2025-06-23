package model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 会議情報と議題・発言・決定事項をまとめて保持するDTO。
 */
public class MeetingDetailsDto implements Serializable {

    private int meetingId;
    private String title;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String participantsText;
    private String detailArea;
    private String decisions;

    private List<AgendaDto> agendas; // 議題リスト

    // ✅ 追加：出力者ID（ログ保存・責任者記録用）
    private int createdBy;

    // --- ゲッター＆セッター ---

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getParticipantsText() {
        return participantsText;
    }

    public void setParticipantsText(String participantsText) {
        this.participantsText = participantsText;
    }

    public String getDetailArea() {
        return detailArea;
    }

    public void setDetailArea(String detailArea) {
        this.detailArea = detailArea;
    }

    public String getDecisions() {
        return decisions;
    }

    public void setDecisions(String decisions) {
        this.decisions = decisions;
    }

    public List<AgendaDto> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<AgendaDto> agendas) {
        this.agendas = agendas;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
