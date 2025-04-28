package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtils {

    private static final String JDBC_URL = "jdbc:h2:./db/test;DB_CLOSE_DELAY=-1";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "";

    public static Connection getConnection() {
        try {
            Class.forName("org.h2.Driver"); // 드라이버 로드 (요즘은 생략해도 됨)
            return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }
}
