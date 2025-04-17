package webserver.http.request;

import webserver.http.common.HttpHeaders;
import webserver.http.common.HttpMethod;
import webserver.http.common.HttpSession;

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

    public HttpSession getSession() {
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

    public void setSession(HttpSession session, boolean isNew) {
        this.session = session;
        this.isNewSession = isNew;
    }

    @Override
    public String toString() {

        return requestLine + "\n" + headers + body;
    }

}
