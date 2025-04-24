package db;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import model.Article;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Map<String, User> users = new HashMap<>();
    private static Map<Integer, Article> articles = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
    public static void deleteAll(){
        users.clear();
    }

    public static void addArticle(Article article){
        articles.put(article.getId(), article);
    }

    public static Article findArticleById(int articleId) {
        return articles.get(articleId);
    }

    public static List<Article> findAllArticles() {
        return articles.values().stream().toList();
    }
    public static Optional<Article> findPreviousArticle(int currentId) {
        return articles.values().stream()
                .filter(article -> article.getId() < currentId)
                .max(Comparator.comparingInt(Article::getId));
    }

    public static Optional<Article> findNextArticle(int currentId) {
        return articles.values().stream()
                .filter(article -> article.getId() > currentId)
                .min(Comparator.comparingInt(Article::getId));
    }
}
