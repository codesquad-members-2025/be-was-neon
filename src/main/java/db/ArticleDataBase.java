package db;

import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ArticleDataBase {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDataBase.class);
    private static long id = 0L;
    private static Map<Long, Article> articles = new ConcurrentHashMap<>();

    public static void addArticle(Article article) {
        articles.put(++id, article);
        article.setId(id);
        logger.debug("Article added: {}", article);
    }

    public static Optional<Article> findArticleById(long id) {
        return Optional.ofNullable(articles.get(id));
    }

    public static Collection<Article> findAll() {
        return articles.values();
    }
}
