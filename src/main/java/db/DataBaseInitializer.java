package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;

public class DataBaseInitializer {
    private static Logger logger = LoggerFactory.getLogger(DataBaseInitializer.class);
    public static void init() {
        try (Connection conn = DataBaseUtils.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "user_id VARCHAR(255) PRIMARY KEY," +
                            "password VARCHAR(255) NOT NULL," +
                            "nickname VARCHAR(255)," +
                            "email VARCHAR(255)" +
                            ")"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS articles (" +
                            "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                            "title VARCHAR(255) NOT NULL," +
                            "content CLOB NOT NULL," +
                            "author_id VARCHAR(255)," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "FOREIGN KEY (author_id) REFERENCES users(user_id)" +
                            ")"
            );

            logger.debug("✅ Database initialized!");

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
