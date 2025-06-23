package util.download;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import model.dto.AgendaDto;
import model.dto.MeetingDetailsDto;

/**
 * 会議議事録をPDF形式で出力するユーティリティクラス。
 */
public class PDFDownloader {

    public static boolean download(MeetingDetailsDto meeting, String filePath, String fontPath) {
        try {
            String html = generateHtml(meeting);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();

            // 実行時パスでフォントを読み込む
            File fontFile = new File(fontPath);
            builder.useFont(fontFile, "Noto Sans JP", 400, PdfRendererBuilder.FontStyle.NORMAL, true);

            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(os.toByteArray());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String generateHtml(MeetingDetailsDto dto) {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='ja'><head>");
        html.append("<meta charset='UTF-8' />")
            .append("<style>")
            .append("body { font-family: 'Noto Sans JP', sans-serif; background: #e0faff; padding: 60px; }")
            .append("h1 { text-align: center; color: #9c4221; font-size: 2.5rem; border-bottom: 3px solid #facc15; padding-bottom: 14px; margin-bottom: 40px; }")
            .append(".info-box { max-width: 700px; margin: 0 auto 40px; border: 2px solid #facc15; border-radius: 12px; background-color: #fffdfa; padding: 24px 32px; }")
            .append(".info-box table { width: 100%; border-collapse: collapse; }")
            .append(".info-box td { padding: 8px 12px; font-size: 1rem; }")
            .append(".info-box td.label { font-weight: bold; width: 25%; color: #333; }")
            .append(".agenda-box { border: 1px dashed #facc15; border-radius: 10px; padding: 20px; margin: 40px auto; background-color: #ffffff; max-width: 700px; page-break-inside: avoid; break-inside: avoid; }")
            .append(".agenda-title { font-weight: 700; font-size: 1.2rem; color: #9c4221; margin-bottom: 14px; border-bottom: 1px solid #ccc; padding-bottom: 6px; }")
            .append(".section-label { font-weight: bold; color: #b45309; margin-top: 14px; }")
            .append(".section-content { margin-top: 6px; background: #f9fafb; border: 1px dotted #ccc; padding: 10px 14px; border-radius: 6px; ")
            .append("overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; white-space: pre-wrap; display: block; width: 100%; box-sizing: border-box; }")
            .append("</style></head><body>");

        // タイトル
        html.append("<h1>議事録</h1>");

        // 会議情報（テーブル形式）
        html.append("<div class='info-box'><table>")
            .append("<tr><td class='label'>会議名：</td><td>").append(escape(dto.getTitle())).append("</td></tr>")
            .append("<tr><td class='label'>日付：</td><td>").append(dto.getMeetingDate().format(dateFmt)).append("</td></tr>")
            .append("<tr><td class='label'>時間：</td><td>").append(dto.getStartTime().format(timeFmt))
            .append(" ～ ").append(dto.getEndTime().format(timeFmt)).append("</td></tr>")
            .append("<tr><td class='label'>参加者：</td><td>").append(escape(dto.getParticipantsText())).append("</td></tr>")
            .append("</table></div>");

        // 議題一覧
        List<AgendaDto> agendas = dto.getAgendas();
        if (agendas != null && !agendas.isEmpty()) {
            for (AgendaDto agenda : agendas) {
                html.append("<div class='agenda-box'>")
                    .append("<div class='agenda-title'>📄 議題：").append(escape(agenda.getTitle())).append("</div>")
                    .append("<div class='section-label'>【発言内容】</div>")
                    .append("<div class='section-content'>").append(escape(agenda.getSpeechNote())).append("</div>")
                    .append("<div class='section-label'>【決定事項】</div>")
                    .append("<div class='section-content'>").append(escape(agenda.getDecisionNote())).append("</div>")
                    .append("</div>");
            }
        } else {
            html.append("<p style='text-align:center;'>議題情報はありません。</p>");
        }

        html.append("</body></html>");
        return html.toString();
    }

    private static String escape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("\n", "<br/>");
    }
}
