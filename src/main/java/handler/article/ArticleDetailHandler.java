package handler.article;

import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.SLASH;

import db.Database;
import handler.Handler;
import java.util.List;
import java.util.Optional;
import model.Article;
import model.User;
import template.ArticleContentRenderer;
import template.HeaderRenderer;
import template.TemplateRenderer;
import webserver.common.HttpStatus;
import webserver.exception.ResourceNotFoundException;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

public class ArticleDetailHandler implements Handler {
    private static final String NO_ARTICLE = "게시글이 없습니다.";
    private final ResourceLoader resourceLoader;

    public ArticleDetailHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        int id = Integer.parseInt(request.getPathVariables().get("id"));
        Optional<Article> article = Database.findArticleById(id);
        if (article.isEmpty()) throw new ResourceNotFoundException(NO_ARTICLE);
        Optional<Article> prev = Database.findPreviousArticle(id);
        Optional<Article> next = Database.findNextArticle(id);

        Session session = getSessionByCookie(request);
        User user = (User) session.getAttribute(SESSION_USER);

        byte[] template = resourceLoader.fileToBytes(SLASH, true);
        List<TemplateRenderer> renderers = List.of(
                new HeaderRenderer(),
                new ArticleContentRenderer(article.get(), prev, next)
        );

        for (TemplateRenderer renderer : renderers) {
            template = renderer.render(user, template);
        }

        return new Response(HttpStatus.OK, template, EMPTY);
    }
}
