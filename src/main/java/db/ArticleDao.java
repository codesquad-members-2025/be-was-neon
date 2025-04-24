package db;

import model.Article;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleDao {

    // Article을 DB에 저장하는 메서드
    public Article save(Article article) {
        String sql = "INSERT INTO article (title, content, author_id) VALUES (?, ?, ?)";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setInt(3, article.getAuthor().getId());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return article.withId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("User ID 생성 실패");
    }

    // Article의 id로 Article 객체를 찾는 메서드
    public Article findById(int id) {
        String sql = "SELECT * FROM article WHERE id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // User는 UserDao를 통해 조회
                User author = new UserDao().findById(rs.getInt("author_id"));
                return new Article(rs.getInt("id"), rs.getString("title"), rs.getString("content"), author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // 모든 Article 리스트를 반환하는 메서드
    public List<Article> findAll() {
        String sql = "SELECT * FROM article";
        List<Article> articles = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User author = new UserDao().findById(rs.getInt("author_id"));
                articles.add(new Article(rs.getInt("id"), rs.getString("title"), rs.getString("content"), author));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return articles;
    }

    // 이전 Article을 조회하는 메서드
    public Optional<Article> findPreviousArticle(int currentId) {
        String sql = "SELECT * FROM article WHERE id < ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User author = new UserDao().findById(rs.getInt("author_id"));
                return Optional.of(new Article(rs.getInt("id"), rs.getString("title"), rs.getString("content"), author));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    // 다음 Article을 조회하는 메서드
    public Optional<Article> findNextArticle(int currentId) {
        String sql = "SELECT * FROM article WHERE id > ? ORDER BY id ASC LIMIT 1";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User author = new UserDao().findById(rs.getInt("author_id"));
                return Optional.of(new Article(rs.getInt("id"), rs.getString("title"), rs.getString("content"), author));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}