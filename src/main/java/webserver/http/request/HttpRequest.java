package webserver.http.request;

import java.util.Map;

import static webserver.http.common.HttpConstants.DOT;

public class HttpRequest {

    private final RequestLine requestLine;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(RequestLine requestLine, Map<String, String> headers, String body) {
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(requestLine).append("\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        sb.append("\n").append(body);

        return sb.toString();
    }

}
