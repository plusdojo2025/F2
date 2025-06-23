package util.download;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.dto.AgendaDto;
import model.dto.MeetingDetailsDto;

/**
 * 議事録をテキストファイルとして出力したり、プレビュー用のテキストを生成するユーティリティクラス。
 */
public class TextDownloader {

    /**
     * 議事録をテキストファイルとして保存する
     */
    public static boolean download(MeetingDetailsDto meeting, String filePath) {
        try {
            File file = new File(filePath);
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                writer.write(generatePreview(meeting));
                writer.flush();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * プレビュー表示用にテキストを生成する（ファイルには書き込まない）
     */
    public static String generatePreview(MeetingDetailsDto meeting) {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        StringBuilder sb = new StringBuilder();

        // ヘッダー：全体幅50文字に合わせて揃える
        sb.append("\n╔════════════════════════════════════════════════════╗\n");
        sb.append("║                    【 議事録 】                    ║\n");
        sb.append("╚════════════════════════════════════════════════════╝\n\n");

        sb.append("■ 会議名　： ").append(meeting.getTitle()).append("\n");
        sb.append("■ 日　付　： ").append(meeting.getMeetingDate().format(dateFmt)).append("\n");
        sb.append("■ 時　間　： ")
          .append(meeting.getStartTime().format(timeFmt)).append(" ～ ")
          .append(meeting.getEndTime().format(timeFmt)).append("\n");
        sb.append("■ 参加者　： ").append(meeting.getParticipantsText()).append("\n");

        sb.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        List<AgendaDto> agendas = meeting.getAgendas();
        if (agendas != null && !agendas.isEmpty()) {
            for (int i = 0; i < agendas.size(); i++) {
                AgendaDto agenda = agendas.get(i);
                sb.append("\n【 議題 ").append(i + 1).append(" 】 ").append(agenda.getTitle()).append("\n");
                sb.append("────────────────────────────────────────────\n");

                sb.append("🗣 発言内容\n");
                String speech = agenda.getSpeechNote().trim();
                if (speech.isEmpty()) {
                    sb.append("　・（発言なし）\n\n");
                } else {
                    for (String line : speech.split("\n")) {
                        sb.append("　・").append(line).append("\n");
                    }
                    sb.append("\n");
                }

                sb.append("✅ 決定事項\n");
                String decision = agenda.getDecisionNote().trim();
                if (decision.isEmpty()) {
                    sb.append("　・（決定事項なし）\n");
                } else {
                    for (String line : decision.split("\n")) {
                        sb.append("　・").append(line).append("\n");
                    }
                }
            }
        } else {
            sb.append("\n※ 議題は登録されていません。\n");
        }

        sb.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("　　　　　　　　　　　　　　　以上\n\n");

        return sb.toString();
    }
}
