package response.handler;

import db.ArticleDao;
import model.User;
import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import session.SessionUserResolver;
import templates.HeaderModelBuilder;
import templates.HtmlBuilder;
import templates.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.HttpHeaders.CONTENT_TYPE;
import static constants.HttpValues.CONTENT_TYPE_HTML;

public class MainPageHandler implements Handler{
    private static final String ARTICLE_LIST = "articleList";
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        String path = request.getRequestHeader().getPath();

        Map<String, String> model = new HashMap<>();

        User user = SessionUserResolver.getSessionUserFromRequest(request).orElse(null);

        HeaderModelBuilder.build(model, user);

        model.put(ARTICLE_LIST,HtmlBuilder.articleList(ArticleDao.findAll()));

        byte[] body = TemplateEngine.render(path, model);

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
