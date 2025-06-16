package model.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class AgendaDto implements Serializable {
	private int agendaId;             // 議題ID
	private int meetingId;            // 会議ID
	private String title;             // 議題名
	private String description;       // 説明
	private int orderNumber;          // 順序
	private String speechNote;        // 発言内容
	private String decisionNote;      // 決定事項
	private boolean isDeleted;        // 削除フラグ
	private Timestamp createdAt;      // 登録日時
	private Timestamp updatedAt;      // 更新日時

	// ゲッターセッター
	public int getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(int agendaId) {
		this.agendaId = agendaId;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSpeechNote() {
		return speechNote;
	}

	public void setSpeechNote(String speechNote) {
		this.speechNote = speechNote;
	}

	public String getDecisionNote() {
		return decisionNote;
	}

	public void setDecisionNote(String decisionNote) {
		this.decisionNote = decisionNote;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	// コンストラクタ
	public AgendaDto() {

	}

	public AgendaDto(int agendaId, int meetingId, String title, String description, int orderNumber, String speechNote,
			String decisionNote, boolean isDeleted, Timestamp createdAt, Timestamp updatedAt) {
		this.agendaId = agendaId;
		this.meetingId = meetingId;
		this.title = title;
		this.description = description;
		this.orderNumber = orderNumber;
		this.speechNote = speechNote;
		this.decisionNote = decisionNote;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
