package db;

import java.util.Collections;
import java.util.List;
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
}
