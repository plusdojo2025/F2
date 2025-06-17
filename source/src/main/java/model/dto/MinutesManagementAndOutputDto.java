package model.dto;

import java.io.Serializable;
import java.sql.Date;


/**
 * 議事録出力画面の検索結果用DTO。
 * meetingsテーブルの一部項目をマッピング。
 */
public class MinutesManagementAndOutputDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int meeting_id;          // 会議ID
    private String title;           // 会議名
    private Date meetingDate;       // 会議開催日

    // ゲッターとセッター
    public int getmeeting_id() {
        return meeting_id;
    }

    public void setmeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
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
