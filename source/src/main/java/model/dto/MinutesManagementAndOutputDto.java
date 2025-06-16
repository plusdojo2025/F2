package model.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class MinutesManagementAndOutputDto {

    private int minuteId;
    private int meetingId;
    private String outputFormat;
    private Timestamp createdAt;

    // 追加フィールド
    private String title;          // 会議名
    private Date meetingDate;      // 会議開催日

    public MinutesManagementAndOutputDto() {
        // デフォルトコンストラクタ
    }

    public MinutesManagementAndOutputDto(int minuteId, int meetingId, String outputFormat, Timestamp createdAt,
                                         String title, Date meetingDate) {
        this.minuteId = minuteId;
        this.meetingId = meetingId;
        this.outputFormat = outputFormat;
        this.createdAt = createdAt;
        this.title = title;
        this.meetingDate = meetingDate;
    }

    // getter/setter

    public int getMinuteId() {
        return minuteId;
    }

    public void setMinuteId(int minuteId) {
        this.minuteId = minuteId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }
}
