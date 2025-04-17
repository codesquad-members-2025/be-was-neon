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

    public HttpRequest(RequestLine requestLine, HttpHeaders headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
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
        String sessionId = CookieShop.takeSessionIdFrom(headers);

        SessionManager sessionManager = SessionManager.getInstance();
        return sessionManager.getOrCreateSession(sessionId);
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {

        return requestLine + "\n" + headers + body;
    }

}
