package handler;

import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.HEADER_COOKIE;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.request.RequestParser;
import webserver.response.Response;
import webserver.session.Session;
import webserver.session.SessionManager;

/**
 * HTTP 요청을 처리하는 핸들러 인터페이스입니다.
 * 모든 HTTP 요청 핸들러는 이 인터페이스를 구현해야 합니다.
 */
public interface Handler {
    Logger logger = LoggerFactory.getLogger(Handler.class);
    String SESSION_ID = "sessionId";
    String SESSION_USER = "sessionUser";
    String NOT_LOGIN_USER = "로그인하지 않은 사용자 입니다.";

    /**
     * HTTP 요청을 처리하고 응답을 반환합니다.
     *
     * @param request 처리할 HTTP 요청
     * @return 처리 결과에 대한 HTTP 응답
     */
    Response handle(Request request);

    /**
     * 요청의 쿠키에서 세션 ID를 추출하여 해당 세션을 반환합니다.
     *
     * @param request HTTP 요청
     * @return 요청에 포함된 세션 ID에 해당하는 세션
     */
    default Session getSessionByCookie(Request request) {
        List<String> cookie = request.getHeaders().get(HEADER_COOKIE);
        Map<String, String> cookieMap = RequestParser.getCookieMap(cookie);
        String sessionId = cookieMap.getOrDefault(SESSION_ID, EMPTY);
        return SessionManager.getInstance().getSession(sessionId);
    }
}
