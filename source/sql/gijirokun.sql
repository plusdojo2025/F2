CREATE DATABASE f2;

USE f2;
-- ユーザーテーブル
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ログイン用のユーザ
INSERT INTO users (username, email, password_hash)
VALUES (
  'dojouser1',
  'dojouser1@plusdojo.jp',
  '5d1a82dba8cefa593f1f9ad97bf98050816e147543ce31f3ca5b15ac3905ca14'
);
-- 会議テーブル
CREATE TABLE meetings (
    meeting_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    meeting_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_by INT NOT NULL,
    participants_text TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);
--詳細のために
ALTER TABLE meetings ADD COLUMN detail_area TEXT AFTER participants_text;
--決定事項のカラムため
ALTER TABLE meetings ADD COLUMN decisions TEXT AFTER detail_area;

-- 議題テーブル
CREATE TABLE agendas (
    agenda_id INT PRIMARY KEY AUTO_INCREMENT,
    meeting_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    order_number INT NOT NULL,
    speech_note TEXT,
    decision_note TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (meeting_id) REFERENCES meetings(meeting_id)
);
-- 議事録管理テーブル
CREATE TABLE MinutesManagementOutput (
    minute_id INT PRIMARY KEY AUTO_INCREMENT,
    meeting_id INT NOT NULL,
    created_by INT NOT NULL,
    output_format VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (meeting_id) REFERENCES meetings(meeting_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);
