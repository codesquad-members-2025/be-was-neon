package response.handler;

import Exceptions.UnAuthorizedException;
import db.ArticleDataBase;
import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import session.SessionUserResolver;
import utils.FormDataParser;

import java.util.Map;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.HttpHeaders.LOCATION;
import static constants.HttpValues.*;

public class CreateArticleHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CreateArticleHandler.class);
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        try {
            Map<String, String> params = FormDataParser.parse(request.getRequestBody());
            String title = params.get("title");
            String content = params.get("content");

            User loginUser = SessionUserResolver.getSessionUserFromRequest(request).orElseThrow(
                    () -> new UnAuthorizedException("User not logged in"));

            Article article = new Article(title, content, loginUser);
            ArticleDataBase.addArticle(article);

            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.FOUND)
                    .header(LOCATION, REDIRECT_INDEX_PATH)
                    .header(CONTENT_LENGTH, EMPTY_BODY_LENGTH)
                    .build();

            responseSender.send(response);
        }catch (UnAuthorizedException e){
            logger.debug("Unauthorized access attempt: {}", e.getMessage());

            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.FOUND)
                    .header(LOCATION, REDIRECT_LOGIN_PAGE)
                    .header(CONTENT_LENGTH, EMPTY_BODY_LENGTH)
                    .build();

            responseSender.send(response);
        }
    }
}
