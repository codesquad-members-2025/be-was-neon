package templates;

import model.Article;

import java.util.Map;

import static templates.ModelConstants.*;

public class ArticleModelBuilder {
    public static Map<String, String> build(Map<String,String> model, Article article) {
        model.put(TITLE, article.getTitle());
        model.put(CONTENT, article.getContent());
        model.put(CREATED_AT, article.getCreatedAt().toString());

        return model;
    }
}
