package model.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * 議事録出力画面の検索結果用DTO。 meetingsテーブルの一部項目をマッピング。
 */
public class MinutesManagementAndOutputDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int meeting_id; // 会議ID
	private String title; // 会議名
	private Date meetingDate; // 会議開催日

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

	// ▼ 議事録出力用 ▼
	private Time startTime;
	private Time endTime;
	private List<String> participants; // カンマ区切りを分割した参加者名リスト
	private List<AgendaDto> agendas;

	public static class AgendaDto implements Serializable {
		private static final long serialVersionUID = 1L;

		private String title;
		private String speechNote;
		private String decisionNote;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
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
	}

	// ▼ getter/setter（必要な部分のみ） ▼
	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

	public List<AgendaDto> getAgendas() {
		return agendas;
	}

	public void setAgendas(List<AgendaDto> agendas) {
		this.agendas = agendas;
	}

	public String generateMinutesText() {
		StringBuilder sb = new StringBuilder();

		// 会議ヘッダー部分
		sb.append("【会議名】").append(title);
		if (meetingDate != null) {
			sb.append("（開催日：").append(meetingDate);
			if (startTime != null && endTime != null) {
				sb.append("　開始：").append(startTime).append(" ～ 終了：").append(endTime);
			}
			sb.append("）");
		}
		sb.append("\n");

		// 参加者
		sb.append("【参加者】");
		if (participants != null && !participants.isEmpty()) {
			sb.append(String.join("、", participants));
		} else {
			sb.append("（未入力）");
		}
		sb.append("\n");

		// 区切り線
		sb.append("--------------------------------------------------------------------------------\n");

		// 議題一覧
		if (agendas != null && !agendas.isEmpty()) {
			for (AgendaDto agenda : agendas) {
				sb.append("■ 議題：").append(nullToEmpty(agenda.getTitle())).append("\n");

				sb.append("＜発言＞\n");
				sb.append(nullToEmpty(agenda.getSpeechNote())).append("\n\n");

				sb.append("＜決定事項＞\n");
				sb.append(nullToEmpty(agenda.getDecisionNote())).append("\n\n");
			}
		} else {
			sb.append("（議題がありません）\n");
		}

		return sb.toString();
	}

	// ヘルパーメソッド：nullなら空文字
	private String nullToEmpty(String input) {
		return input == null ? "" : input;
	}

}
