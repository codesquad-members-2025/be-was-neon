package db;


import Exceptions.HttpException;
import model.Article;
import model.User;
import response.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleDao {

    public static void save(Article article) {
        String sql = "INSERT INTO articles (title, content, author_id) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getAuthor().getUserId());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    article.setId(id); // Article 객체에도 id 설정
                }
            }

        } catch (SQLException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to save article");
        }
    }

    public static Optional<Article> findById(long id) {
        String sql = "SELECT * FROM articles WHERE id = ?";

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String authorId = rs.getString("author_id");

                    User author = UserDao.findByUserId(authorId)
                            .orElseThrow(() -> new HttpException(Status.INTERNAL_SERVER_ERROR, "Author not found: " + authorId));

                    Article article = new Article(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            author,
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    return Optional.of(article);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to find article");
        }
    }

    public static List<Article> findAll() {
        String sql = "SELECT * FROM articles ORDER BY id DESC"; // 최신순 정렬(optional)

        List<Article> articles = new ArrayList<>();

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String authorId = rs.getString("author_id");

                User author = UserDao.findByUserId(authorId)
                        .orElseThrow(() -> new HttpException(Status.INTERNAL_SERVER_ERROR, "Author not found: " + authorId));

                Article article = new Article(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        author,
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                articles.add(article);
            }

            return articles;

        } catch (SQLException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to find all articles");
        }
    }
}
