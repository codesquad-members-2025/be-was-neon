package template;

import db.Database;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import model.Article;
import model.User;

public class ArticleContentRenderer implements TemplateRenderer {
    private static final String CONTENT_PLACEHOLDER = "{{article_content}}";
    private static final String AUTHOR_PLACEHOLDER = "{{article_author}}";
    private static final String PREV_ARTICLE_PLACEHOLDER = "{{prev_article_link}}";
    private static final String NEXT_ARTICLE_PLACEHOLDER = "{{next_article_link}}";
    private static final String IMAGE_PATH = "{{image_path}}";
    private final Article article;
    private final Optional<Article> prev;
    private final Optional<Article> next;

    public ArticleContentRenderer(Article article, Optional<Article> prev, Optional<Article> next) {
        this.article = article;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public byte[] render(User user, byte[] template) {
        String html = new String(template, StandardCharsets.UTF_8);

        String content = articleContentToHtml(article);

        String author = article.getAuthor().getName();

        String replaced = html.replace(CONTENT_PLACEHOLDER, content)
                .replace(AUTHOR_PLACEHOLDER, author)
                .replace(PREV_ARTICLE_PLACEHOLDER, prev.map(a -> "/" + a.getId()).orElse("#"))
                .replace(NEXT_ARTICLE_PLACEHOLDER, next.map(a -> "/" + a.getId()).orElse("#"))
                .replace(IMAGE_PATH, article.getImageUrl());

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