package db;

import Exceptions.HttpException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.Status;

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
            logger.debug("User saved: {}", user);

        } catch (SQLException e) {
            logger.error("Failed to save user: {}", user, e);
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to save user");
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
                    logger.debug("User found: {}", user);
                    return Optional.of(user);
                } else {
                    logger.debug("No user found with userId: {}", userId);
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to find user by userId: {}", userId, e);
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to find user");
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

            logger.debug("All users found: total {} users", users.size());
            return users;

        } catch (SQLException e) {
            logger.error("Failed to find all users", e);
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to find all users");
        }
    }
}
