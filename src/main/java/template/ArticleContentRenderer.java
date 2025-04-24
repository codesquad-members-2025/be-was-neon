package template;

import db.Database;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import model.Article;
import model.User;

public class ArticleContentRenderer implements TemplateRenderer {
    private static final String CONTENT_PLACEHOLDER = "{{article_content}}";
    private static final String AUTHOR_PLACEHOLDER = "{{article_author}}";

    @Override
    public byte[] render(User user, byte[] template) {
        String html = new String(template, StandardCharsets.UTF_8);
        Article article = Database.findAllArticles().reversed().getFirst();
        String content = articleContentToHtml(article);

        String author = article.getAuthor().getName();

        String replaced = html.replace(CONTENT_PLACEHOLDER, content).replace(AUTHOR_PLACEHOLDER, author);
        return replaced.getBytes(StandardCharsets.UTF_8);

    }

    private String articleContentToHtml(Article article) {
        return """
                <p class="post__title">""" +
                article.getTitle() + """
                  </p>
                  <p class="post__article">""" +
                article.getContent() + """
                    </p>
                    """;
    }


} 