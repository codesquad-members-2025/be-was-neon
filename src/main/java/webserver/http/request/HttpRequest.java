package webserver.http.request;

import webserver.http.common.CookieShop;
import webserver.http.common.HttpHeaders;
import webserver.http.common.HttpMethod;
import webserver.http.common.HttpSession;
import webserver.session.SessionManager;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeaders headers;
    private final String body;
    private HttpSession session;
    private boolean isNewSession;

    public HttpRequest(RequestLine requestLine, HttpHeaders headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
        this.session = null;
        this.isNewSession = false;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public HttpSession getOrCreateSession() {
        if (this.session != null) {
            return session;
        }

        String sessionId = CookieShop.takeSessionIdFrom(headers);
        if (sessionId == null) {
            isNewSession = true;
        }

        SessionManager sessionManager = SessionManager.getInstance();
        session = sessionManager.getOrCreateSession(sessionId);

        return session;
    }

    public String getBody() {
        return body;
    }

    public String getSessionId() {
        return session.getId();
    }

    public boolean isNewSession() {
        return isNewSession;
    }

    @Override
    public String toString() {

        return requestLine + "\n" + headers + body;
    }

}
