package model.service;

import java.sql.Connection;

import model.dao.UserDao;
import model.dto.UserDto;
import util.Validator;

/**
 * ユーザーに関するビジネスロジックを提供するサービスクラス
 */
public class UserService extends BaseService {

    /**
     * ログイン認証
     */
    public UserDto login(String email, String password) throws Exception {
        if (!Validator.isEmail(email) || Validator.isEmpty(password)) {
            throw new IllegalArgumentException("メールまたはパスワード形式が正しくありません。");
        }

        try (Connection conn = getConnection()) {
            UserDao dao = new UserDao(conn);
            UserDto user = dao.findByEmail(email);
            if (user != null && Validator.checkPassword(password, user.getPasswordHash())) {
                return user;
            }
            return null;
        }
    }

    /**
     * ユーザー登録
     */
    public void register(UserDto dto) throws Exception {
        String plainPassword = dto.getPasswordHash();  // ← 平文を取り出す

        if (!Validator.isEmail(dto.getEmail()) || !Validator.isPasswordComplex(plainPassword)) {
            throw new IllegalArgumentException("メール形式またはパスワード形式が不正です。");
        }

        try (Connection conn = getConnection()) {
            UserDao dao = new UserDao(conn);
            if (dao.findByEmail(dto.getEmail()) != null) {
                throw new IllegalArgumentException("このメールアドレスは既に登録されています。");
            }

            // ここで初めてハッシュ化してセット
            dto.setPasswordHash(Validator.hashPassword(plainPassword));

            dao.insert(dto);
        }
    }



    /**
     * メールアドレスの存在確認
     */
    public boolean exists(String email) throws Exception {
        try (Connection conn = getConnection()) {
            return new UserDao(conn).findByEmail(email) != null;
        }
    }

    /**
     * ユーザー情報の更新
     */
    public void update(UserDto dto) throws Exception {
        try (Connection conn = getConnection()) {
            new UserDao(conn).update(dto);
        }
    }
}
