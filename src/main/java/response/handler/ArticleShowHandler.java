package response.handler;

import Exceptions.HttpException;
import db.ArticleDao;
import model.Article;
import model.User;
import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import session.SessionUserResolver;
import templates.ArticleModelBuilder;
import templates.HeaderModelBuilder;
import templates.TemplateEngine;
import utils.HttpRequestParser;

import java.util.HashMap;
import java.util.Map;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.HttpHeaders.CONTENT_TYPE;
import static constants.HttpValues.CONTENT_TYPE_HTML;

public class ArticleShowHandler implements Handler {
    private static final String TEMPLATE = "/article/show.html";

    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        String path = request.getRequestHeader().getPath();
        Map<String, String> model = new HashMap<>();

        User user = SessionUserResolver.getSessionUserFromRequest(request)
                .orElseThrow(() -> new HttpException(Status.UNAUTHORIZED,request,"Unauthorized"));

        Article article = HttpRequestParser.parsePathVariable(path, 1)
                .map(HttpRequestParser::parseLong)
                .flatMap(ArticleDao::findById)
                .orElseThrow(() -> new HttpException(Status.NOT_FOUND, request, "Article not found"));

        HeaderModelBuilder.build(model, user);
        ArticleModelBuilder.build(model, article);

        byte[] body = TemplateEngine.render(TEMPLATE, model);

        Response response = Response.builder()
                .httpVersion(request.getRequestHeader().getHttpVersion())
                .status(Status.OK)
                .header(CONTENT_TYPE, CONTENT_TYPE_HTML)
                .header(CONTENT_LENGTH, Integer.toString(body.length))
                .body(body)
                .build();

        responseSender.send(response);
    }
}
