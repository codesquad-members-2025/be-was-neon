package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        createTablesIfNotExist(conn);  // 테이블 생성 함수 호출
        return conn;
    }

    private static void createTablesIfNotExist(Connection conn) {
        // 'user' 테이블 생성 SQL
        String createUserTableSql = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                user_id VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                name VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
            );
        """;

        // 'article' 테이블 생성 SQL
        String createArticleTableSql = """
            CREATE TABLE IF NOT EXISTS article (
                id INT PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(255) NOT NULL,
                content TEXT NOT NULL,
                author_id INT NOT NULL,
                FOREIGN KEY (author_id) REFERENCES users(id)
            );
        """;

        // 테이블 생성 SQL 실행
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUserTableSql);  // user 테이블 생성
            stmt.execute(createArticleTableSql);  // article 테이블 생성
        } catch (SQLException e) {
            throw new RuntimeException("Table creation failed", e);
        }
    }
}
