package db;

import Exceptions.HttpException;
import model.Article;
import model.User;
import response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleDao {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDao.class);

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
                    logger.debug("Article saved with ID: {}", id);
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to save article: {}", article, e);
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
                            .orElseThrow(() -> {
                                logger.error("Author not found for authorId: {}", authorId);
                                return new HttpException(Status.INTERNAL_SERVER_ERROR, "Author not found: " + authorId);
                            });

                    Article article = new Article(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            author,
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    logger.debug("Article found: {}", article);
                    return Optional.of(article);
                } else {
                    logger.debug("No article found with ID: {}", id);
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to find article by ID: {}", id, e);
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to find article");
        }
    }

    public static List<Article> findAll() {
        String sql = "SELECT * FROM articles ORDER BY id DESC";

        List<Article> articles = new ArrayList<>();

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String authorId = rs.getString("author_id");

                User author = UserDao.findByUserId(authorId)
                        .orElseThrow(() -> {
                            logger.error("Author not found for authorId: {}", authorId);
                            return new HttpException(Status.INTERNAL_SERVER_ERROR, "Author not found: " + authorId);
                        });

                Article article = new Article(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        author,
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                articles.add(article);
            }

            logger.debug("Total articles found: {}", articles.size());
            return articles;

        } catch (SQLException e) {
            logger.error("Failed to find all articles", e);
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "Failed to find all articles");
        }
    }
}
