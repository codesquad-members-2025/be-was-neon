package webserver.resolver;

import webserver.http.common.CookieShop;
import webserver.http.common.HttpHeaders;
import webserver.http.common.HttpSession;
import webserver.http.request.HttpRequest;
import webserver.session.SessionManager;

public class SessionResolver {

    private static final SessionManager sessionManager = SessionManager.getInstance();

    private SessionResolver() {
    }

    public static void injectSession(HttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String sid = CookieShop.takeSessionIdFrom(headers);
        boolean isNew = (sid == null);

        HttpSession session = sessionManager.getOrCreateSession(sid);
        request.setSession(session, isNew);
    }

    public static void addSessionCookieIfNew(HttpRequest request, ResolveResponse<?> resolveResponse) {
        if (!request.isNewSession()) {
            return;
        }

        HttpHeaders headers = resolveResponse.getHeaders();
        String sessionId = request.getSessionId();
        headers.addCookie(sessionId);
    }

}
