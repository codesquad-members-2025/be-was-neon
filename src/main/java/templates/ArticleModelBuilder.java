package templates;

import model.Article;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import static templates.ModelConstants.*;

public class ArticleModelBuilder {
    public static Map<String, String> build(Map<String,String> model, Article article) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        model.put(NICKNAME, article.getAuthor().getNickname());
        model.put(TITLE, article.getTitle());
        model.put(CONTENT, article.getContent());
        model.put(CREATED_AT, article.getCreatedAt().format(formatter));

        return model;
    }
}
