package template;

import db.Database;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import model.Article;
import model.User;

public class ArticleContentRenderer implements TemplateRenderer {
    private static final String CONTENT_PLACEHOLDER = "{{article_content}}";

    @Override
    public byte[] render(User user, byte[] template) {
        String html = new String(template, StandardCharsets.UTF_8);
        Article article = Database.findAllArticles().reversed().getFirst();
        String content = """
                <p class="post__title">""" +
                article.getTitle() + """
                  </p>
                  <p class="post__article">""" +
                article.getContent() + """
                    </p>
                    """;

        String replaced = html.replace(CONTENT_PLACEHOLDER, content);
        return replaced.getBytes(StandardCharsets.UTF_8);

    }


} 