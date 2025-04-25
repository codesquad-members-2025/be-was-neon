package handler;

import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.HEADER_COOKIE;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;
import webserver.request.Request;
import webserver.request.RequestParser;
import webserver.response.Response;
import webserver.session.Session;
import webserver.session.SessionManager;

public interface Handler {
    Logger logger = LoggerFactory.getLogger(Handler.class);
    String SESSION_ID = "sessionId";
    String SESSION_USER = "sessionUser";
    String NOT_LOGIN_USER = "로그인하지 않은 사용자 입니다.";
    Response handle(Request request);

    default Session getSessionByCookie(Request request) {
        List<String> cookie = request.getHeaders().get(HEADER_COOKIE);
        Map<String, String> cookieMap = RequestParser.getCookieMap(cookie);
        String sessionId = cookieMap.getOrDefault(SESSION_ID, EMPTY);
        return SessionManager.getInstance().getSession(sessionId);
    }
}
