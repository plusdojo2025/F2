package model.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class MeetingDto implements Serializable {
    private int meetingId;
    private String title;
    private Date meetingDate;
    private Time startTime;
    private Time endTime;
    private int createdBy;
    private String participantsText;
    private boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String detailArea;
    private String decisions;

    public MeetingDto() {}

    public MeetingDto(int meetingId, String title, Date meetingDate, Time startTime, Time endTime, int createdBy, String participantsText, boolean isDeleted, Timestamp createdAt, Timestamp updatedAt, String detailArea, String decisions) {
        this.meetingId = meetingId;
        this.title = title;
        this.meetingDate = meetingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = createdBy;
        this.participantsText = participantsText;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.detailArea = detailArea;
        this.decisions = decisions;
    }

    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Date getMeetingDate() { return meetingDate; }
    public void setMeetingDate(Date meetingDate) { this.meetingDate = meetingDate; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public String getParticipantsText() { return participantsText; }
    public void setParticipantsText(String participantsText) { this.participantsText = participantsText; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getDetailArea() { return detailArea; }
    public void setDetailArea(String detailArea) { this.detailArea = detailArea; }
    
    public String getDecisions() { return decisions; }
    public void setDecisions(String decisions) { this.decisions = decisions; }
}