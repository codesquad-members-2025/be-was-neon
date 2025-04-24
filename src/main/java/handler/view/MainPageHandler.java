package handler.view;

import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.SLASH;

import db.Database;
import handler.Handler;
import java.util.List;
import java.util.Optional;
import model.Article;
import template.ArticleContentRenderer;
import template.HeaderRenderer;
import template.TemplateRenderer;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

public class MainPageHandler implements Handler {
    private final ResourceLoader resourceLoader;

    public MainPageHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        Article article = Database.findAllArticles().reversed().getFirst();
        Optional<Article> prev = Database.findPreviousArticle(article.getId());
        Optional<Article> next = Database.findNextArticle(article.getId());

        byte[] html = resourceLoader.fileToBytes(SLASH, true);
        List<TemplateRenderer> renderers = List.of(
                new HeaderRenderer(),
                new ArticleContentRenderer(article, prev, next)
        );

        for (TemplateRenderer renderer : renderers) {
            html = renderer.render(null, html);
        }

        return new Response(HttpStatus.OK, html, EMPTY);
    }
}