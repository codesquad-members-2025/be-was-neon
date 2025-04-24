package handler;

import static webserver.common.Constants.SLASH;

import db.Database;
import model.Article;
import model.User;
import webserver.common.HttpStatus;
import webserver.exception.UnauthorizedUserException;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

public class WriteArticleHandler implements Handler{
    @Override
    public Response handle(Request request) {
        Session session = getSessionByCookie(request);
        User user = (User) session.getAttribute(SESSION_USER);
        if (user == null) {
            throw new UnauthorizedUserException(NOT_LOGIN_USER);
        }

        String content = request.getBody().get("content");
        String title = request.getBody().get("title");
        Database.addArticle(new Article(title, content, user));
        logger.debug("content : {}", content);

        return new Response(HttpStatus.FOUND, SLASH);
    }
}
