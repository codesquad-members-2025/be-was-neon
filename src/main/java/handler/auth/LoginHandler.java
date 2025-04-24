package handler.auth;

import static webserver.common.Constants.BLANK;
import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.EQUAL;
import static webserver.common.Constants.HEADER_COOKIE;
import static webserver.common.Constants.PASSWORD;
import static webserver.common.Constants.SEMI_COLON;
import static webserver.common.Constants.SLASH;
import static webserver.common.Constants.USER_ID;

import db.Database;
import handler.Handler;
import model.User;
import webserver.common.HttpStatus;
import webserver.exception.NotRegisteredUserException;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

public class LoginHandler implements Handler {
    private static final String NOT_REGISTERED_USER = "아이디 또는 비밀번호가 올바르지 않습니다.";
    private static final String ROOT_PATH = "Path=/";
    @Override
    public Response handle(Request request) {

        String userId = request.getBody().get(USER_ID);
        String password = request.getBody().get(PASSWORD);

        User user = Database.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            throw new NotRegisteredUserException(NOT_REGISTERED_USER);
        }

        Session session = getSessionByCookie(request);

        session.setAttribute(SESSION_USER, user);
        String cookies = SESSION_ID + EQUAL +session.getSessionId() + SEMI_COLON + BLANK + ROOT_PATH;

        return new Response(HttpStatus.FOUND, SLASH, cookies);
    }
}
