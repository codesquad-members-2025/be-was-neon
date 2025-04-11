package webserver.http.request;

import webserver.http.common.HttpHeaders;

import static webserver.http.common.HttpConstants.DOT;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeaders headers;
    private final String body;

    public HttpRequest(RequestLine requestLine, HttpHeaders headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public boolean isResourceRequest() {
        return requestLine.getPath().contains(DOT);
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {

        return requestLine + "\n" + headers + body;
    }

}
