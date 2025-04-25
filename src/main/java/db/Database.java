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
    private static UserDao userDao = new UserDao();
    private static ArticleDao articleDao = new ArticleDao();

    public static User addUser(User user) {
        return userDao.save(user);
    }

    public static User findUserById(String userId) {
        return userDao.findByUserId(userId);
    }

    public static Collection<User> findAll() {
        return userDao.findAll();
    }
    public static void deleteAll(){
        userDao.deleteAll();
    }

    public static Article addArticle(Article article){
        return articleDao.save(article);
    }

    public static Article findArticleById(int articleId) {
        return articleDao.findById(articleId);
    }

    public static List<Article> findAllArticles() {
        return articleDao.findAll();
    }
    public static Optional<Article> findPreviousArticle(int currentId) {
        return articleDao.findPreviousArticle(currentId);
    }

    public static Optional<Article> findNextArticle(int currentId) {
        return articleDao.findNextArticle(currentId);
    }

    public static void deleteAllArticles() {
        articleDao.deleteAllArticles();
    }
}
