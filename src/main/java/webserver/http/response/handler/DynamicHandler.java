package webserver.http.response.handler;

import db.Database;
import exception.InvalidHttpMethodException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileContentUtil;
import webserver.http.common.ContentType;
import webserver.http.common.StatusCode;
import webserver.http.common.UrlPattern;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.ResponseBuilder;

import java.util.Map;
import java.util.Optional;

public class DynamicHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(DynamicHandler.class);

    @Override
    public Response handle(Request request) {
        String path = request.getRequestLine("path");
        String method = request.getRequestLine("method");
        Map<String, String> body = request.getBody();

        try {
            if (path.equals(UrlPattern.CREATE_USER.getPattern())) {
                createUser(method, body);
            }
        } catch (InvalidHttpMethodException e) {
            logger.error(e.getMessage());
            Optional<byte[]> errorBody = FileContentUtil.getFileContent("error/400.html");
            return new ResponseBuilder(StatusCode.BAD_REQUEST, errorBody.get(), ContentType.HTML.getContentType()).build();
        }

        return new ResponseBuilder(StatusCode.FOUND, "/").build();
    }

    private void createUser(String method, Map<String, String> body) {

        if (!"POST".equals(method)) {
            throw new InvalidHttpMethodException("지원하지 않는 HTTP 메서드입니다.");
        }

        String userId = (body.get("userId"));
        String password = (body.get("password"));
        String name = (body.get("name"));
        String email = (body.get("email"));
        User user = new User(userId, password, name, email);
        Database.addUser(user);

    }

}
