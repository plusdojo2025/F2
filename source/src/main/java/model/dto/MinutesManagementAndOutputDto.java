package model.dto;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
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
		sb.append(centerAlignAscii(title, TOTAL_WIDTH)).append("\n");

		if (meetingDate != null) {
		    String dateLine = String.format("（開催日：%s　開始：%s ～ 終了：%s）",
		        meetingDate.toString(),
		        startTime != null ? startTime.toString() : "",
		        endTime != null ? endTime.toString() : "");
		    sb.append(centerAlignAscii(dateLine, TOTAL_WIDTH)).append("\n");
		}

		String participantsLine = "【参加者】" + 
		    (participants != null && !participants.isEmpty() ? String.join("、", participants) : "（未入力）");
		sb.append(centerAlignAscii(participantsLine, TOTAL_WIDTH)).append("\n");
		// 区切り線
		sb.append("-------------------------------------------------------------------------------------------------------------------\n");

		 if (agendas == null || agendas.isEmpty()) {
		        sb.append("（議題がありません）\n");
		        return sb.toString();
		    }
		 
		// 議題を2つずつ処理
		    for (int i = 0; i < agendas.size(); i += 2) {
		        // 左の議題
		        AgendaDto left = agendas.get(i);
		        // 右の議題（ある場合）
		        AgendaDto right = (i + 1 < agendas.size()) ? agendas.get(i + 1) : null;

		        // 議題タイトル
		        String leftTitle = "■ 議題：" + nullToEmpty(left.getTitle());
		        String rightTitle = right != null ? "■ 議題：" + nullToEmpty(right.getTitle()) : "";

		        sb.append(padRightVisual(leftTitle, COLUMN_WIDTH)).append("｜ ")
		        .append(padRightVisual(rightTitle, COLUMN_WIDTH)).append("\n");
		        
		        // 発言ラベル
		        sb.append(padRightVisual("＜発言＞", COLUMN_WIDTH)).append("｜ ")
		        .append(padRightVisual("＜発言＞", COLUMN_WIDTH)).append("\n");

		        // 発言内容（改行を考慮し、行単位で並べる）
		        String[] leftSpeeches = (left.getSpeechNote() != null) ? left.getSpeechNote().split("\n") : new String[]{""};
		        String[] rightSpeeches = (right != null && right.getSpeechNote() != null) ? right.getSpeechNote().split("\n") : new String[]{""};

		        int maxLines = Math.max(leftSpeeches.length, rightSpeeches.length);
		     // 発言内容（行単位）
		        for (int line = 0; line < maxLines; line++) {
		            String leftLine = (line < leftSpeeches.length) ? leftSpeeches[line] : "";
		            String rightLine = (line < rightSpeeches.length) ? rightSpeeches[line] : "";
		            sb.append(padRightVisual(leftLine, COLUMN_WIDTH)).append("｜ ")
		              .append(padRightVisual(rightLine, COLUMN_WIDTH)).append("\n");
		        }


		        sb.append("\n");

		        // 決定事項ラベル
		        sb.append(padRightVisual("＜決定事項＞", COLUMN_WIDTH)).append("｜ ")
		        .append(padRightVisual("＜決定事項＞", COLUMN_WIDTH)).append("\n");

		        // 決定事項内容（同様に行単位で）
		        String[] leftDecisions = (left.getDecisionNote() != null) ? left.getDecisionNote().split("\n") : new String[]{""};
		        String[] rightDecisions = (right != null && right.getDecisionNote() != null) ? right.getDecisionNote().split("\n") : new String[]{""};

		        maxLines = Math.max(leftDecisions.length, rightDecisions.length);
		        for (int line = 0; line < maxLines; line++) {
		            String leftLine = (line < leftDecisions.length) ? leftDecisions[line] : "";
		            String rightLine = (line < rightDecisions.length) ? rightDecisions[line] : "";
		            sb.append(padRightVisual(leftLine, COLUMN_WIDTH)).append("｜ ")
		              .append(padRightVisual(rightLine, COLUMN_WIDTH)).append("\n");
		        }
		        sb.append("-------------------------------------------------------------------------------------------------------------------\n");
		    }

		    return sb.toString();
		}

	// 中央寄せ（半角スペース）
	private String centerAlignAscii(String text, int totalWidth) {
	    int visualLength = getVisualLength(text);
	    int padding = Math.max((totalWidth - visualLength) / 2, 0);
	    return " ".repeat(padding) + text;
	}

    // 文字列の見た目幅（全角:2、半角:1）
    private int getVisualLength(String text) {
        if (text == null) return 0;
        int len = 0;
        for (char c : text.toCharArray()) {
            len += (String.valueOf(c).getBytes(StandardCharsets.UTF_8).length > 1) ? 2 : 1;
        }
        return len;
    }
    
 // ▼ 定数：列の見た目幅
    private static final int COLUMN_WIDTH = 45;
    private static final int TOTAL_WIDTH = COLUMN_WIDTH * 2 + 2;


    // 文字列を指定の見た目幅で右詰め（全角:2、半角:1）
    private String padRightVisual(String text, int visualWidth) {
        if (text == null) text = "";
        int padding = visualWidth - getVisualLength(text);
        if (padding <= 0) return text;
        return text + " ".repeat(padding);
    }



    /* 文字繰り返しユーティリティ  一応作成したけどJava11以降だと標準メソッドで解決できるのでコメント化
     * 繰り返しがうまくいかなかったらコメント外してください
    private String repeat(String s, int count) {
        return new String(new char[count]).replace("\0", s);
    }*/
    

	// ヘルパーメソッド：nullなら空文字
	private String nullToEmpty(String input) {
	    return input == null ? "" : input;
	}
	
	
	// ▼ ログ出力用 ▼
	private int createdBy;
	private String outputFormat;

	public int getCreatedBy() {
	    return createdBy;
	}

	public void setCreatedBy(int createdBy) {
	    this.createdBy = createdBy;
	}

	public String getOutputFormat() {
	    return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
	    this.outputFormat = outputFormat;
	}


}
