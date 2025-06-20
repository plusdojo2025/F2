package model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import model.dao.MinutesManagementOutputDao;
import model.dto.MinutesManagementAndOutputDto;

/**
 * 議事録ダウンロード関連のサービスクラス。
 */
public class DownloadService extends BaseService {

	/**
	 * 会議の検索処理
	 * 
	 * @param searchName 会議名（部分一致）
	 * @param searchDate 会議日付（yyyy-MM-dd）
	 * @return 該当する会議リスト
	 */
	public List<MinutesManagementAndOutputDto> searchMeetings(String searchName, String searchDate) {
		try (Connection conn = getConnection()) {
			MinutesManagementOutputDao dao = new MinutesManagementOutputDao(conn);
			return dao.searchMeetings(searchName, searchDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * テキスト形式の議事録ファイルを生成し、一時ファイルとして返す。
	 * 
	 * @param dto 議事録情報（会議と議題）
	 * @return 生成された一時ファイル
	 * @throws IOException ファイル書き込み失敗時
	 */
	public File generateMinutesTextFile(MinutesManagementAndOutputDto dto) throws IOException {
		// 議事録テキストを生成
		String text = dto.generateMinutesText();

		// 日付を文字列に変換
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String meetingDateStr = sdf.format(dto.getMeetingDate());

		// 会議名・日付からファイル名作成（禁止文字を置換）
		String safeTitle = dto.getTitle().replaceAll("[\\\\/:*?\"<>|]", "_");
		String safeDate = meetingDateStr.replaceAll("[\\\\/:*?\"<>|]", "_");

		String fileName = safeTitle + " " + safeDate + ".txt";

		// 一時フォルダにファイル作成
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile = new File(tempDir, fileName);

		// ファイル書き込み
		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(text.getBytes(StandardCharsets.UTF_8));
		}

		return tempFile;
	}

	public MinutesManagementAndOutputDto getMeetingDetails(int meetingId) throws SQLException {
		try (Connection conn = getConnection()) {
			MinutesManagementOutputDao dao = new MinutesManagementOutputDao(conn);
			return dao.findMeetingDetailsById(meetingId);
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * テキストファイルを生成し、DBに出力ログを記録する
	 */
	public File generateTextAndLog(MinutesManagementAndOutputDto dto) throws Exception {
	    File file = generateMinutesTextFile(dto); // ファイル出力処理

	    try (Connection conn = getConnection()) {
	        MinutesManagementOutputDao dao = new MinutesManagementOutputDao(conn);
	        dao.insertOutputLog(conn, dto); // ログをDBに記録
	    }

	    return file;
	}


	/**
	 * プレビュー表示用の議事録テキストを取得
	 * @param meetingId 対象会議ID
	 * @param format 出力形式（"text" / "pdf"）
	 * @return 議事録テキスト（今はtextのみ対応）
	 */
	public String getMinutesPreviewText(int meetingId, String format) throws SQLException, IOException {
		MinutesManagementAndOutputDto dto = getMeetingDetails(meetingId);
		if (dto == null) {
			return null;
		}

		if ("text".equals(format)) {
			return dto.generateMinutesText(); // DTO内で定義されたテキスト生成メソッドを活用
		} else if ("pdf".equals(format)) {
			// 将来的にPDFの場合のプレビュー取得処理を実装（例：Base64に変換など）
			throw new UnsupportedOperationException("PDFプレビューは未実装です。");
		} else {
			throw new IllegalArgumentException("不正な出力形式です。");
		}
	}

}
