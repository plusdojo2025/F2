# 議事録支援アプリ「ぎじろくん」

## 概要
「ぎじろくん」は、会議の議事録を効率的に作成・管理・出力するWebアプリケーションです。  
ユーザーは会議を登録し、発言・決定事項を記録して、PDFやText形式で出力できます。

## 主な機能
- ユーザー登録・ログイン・設定変更
- 会議の作成・編集・削除・検索
- 議題・発言・決定事項の登録
- 議事録プレビューとダウンロード（PDF / Text）
- ダッシュボードによる進捗把握

## 使用技術
- Java 11+
- JSP / Servlet (アノテーションベース)
- JDBC（MySQL連携）
- JSTL
- Gradle
- Apache POI / OpenPDF（出力機能）
- CSS / JavaScript（画面スタイル）

## ディレクトリ構成（抜粋）
├── src/
│ ├── main/
│ │ ├── java/ # Javaコード一式（Servlet, DAO, Serviceなど）
│ │ ├── webapp/ # JSP画面・静的ファイル（CSS/JS）
│ │ └── resources/ # 設定ファイル（メッセージなど）
├── README.md
├── build.gradle


## セットアップ・起動方法

1. リポジトリをクローン
2. データベースを作成 & 初期SQLを実行
3. `gradle build` でビルド
4. サーバーにデプロイ（Tomcat推奨）

## 開発者向けメモ

- サーブレットは `/controller/` 配下にパッケージごと整理
- ビジネスロジックは `/service/` 配下のクラスに集中
- JSPのパスは `/WEB-INF/jsp/` 配下に画面単位で分離
- データアクセスは DAOパターン
- 共通処理（接続、バリデーションなど）は `/util/` に配置

 必要なJAR:
     - jstl-1.2.jar
     - standard-1.1.2.jar
     - mysql-connector-java-8.0.26.jar
     これらをWEB-INF/libに配置してください。

