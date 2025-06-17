package model.service;

import java.sql.Connection;
import java.util.List;

import model.dao.MinutesManagementOutputDao;
import model.dto.MinutesManagementAndOutputDto;

/**
 * 議事録ダウンロード関連のサービスクラス。
 */
public class DownloadService extends BaseService {

    /**
     * 会議の検索処理
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
}
