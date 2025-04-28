package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public static void save(User user) {
        String sql = "INSERT INTO users (user_id, password, nickname, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    public static Optional<User> findByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("user_id"),
                            rs.getString("nickname"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                    return Optional.of(user);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user", e);
        }
    }

    public static List<User> findAll() {
        String sql = "SELECT * FROM users";

        List<User> users = new ArrayList<>();

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getString("user_id"),
                        rs.getString("nickname"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all users", e);
        }
    }
}
