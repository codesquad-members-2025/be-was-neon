package handler;

import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.HEADER_COOKIE;

import java.util.List;
import java.util.Map;
import webserver.request.Request;
import webserver.request.RequestParser;
import webserver.response.Response;
import webserver.session.Session;
import webserver.session.SessionManager;

public interface Handler {
    String SESSION_ID = "sessionId";
    Response handle(Request request);

    default Session getSessionByCookie(Request request) {
        List<String> cookie = request.getHeaders().get(HEADER_COOKIE);
        Map<String, String> cookieMap = RequestParser.getCookieMap(cookie);
        String sessionId = cookieMap.getOrDefault(SESSION_ID, EMPTY);
        return SessionManager.getInstance().getSession(sessionId);
    }
}
