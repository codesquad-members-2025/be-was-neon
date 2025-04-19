package provider;

import webserver.http.common.HttpHeaders;
import webserver.http.common.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.request.RequestLine;

import static webserver.http.common.HttpConstants.SPACE;

public class RequestBuilder {

    private HttpMethod method = HttpMethod.GET;
    private String path = "/";
    private String query = "";
    private String httpVer = "HTTP/1.1";
    private final HttpHeaders headers = new HttpHeaders();
    private String body = "";

    public static RequestBuilder get(String path) {
        return new RequestBuilder().method(HttpMethod.GET).path(path);
    }

    public static RequestBuilder post(String path) {
        return new RequestBuilder().method(HttpMethod.POST).path(path);
    }

    public RequestBuilder method(HttpMethod m) {
        this.method = m;
        return this;
    }

    public RequestBuilder path(String p) {
        this.path = p;
        return this;
    }

    public RequestBuilder query(String q) {
        this.query = q.startsWith("?") ? q : "?" + q;
        return this;
    }

    public RequestBuilder header(String k, String v) {
        headers.add(k, v);
        return this;
    }

    public RequestBuilder body(String b) {
        this.body = b;
        return this;
    }

    public HttpRequest build() {
        String uri = query.isEmpty() ? path : path + query;
        String rawLine = method + SPACE + uri + SPACE + httpVer;
        RequestLine rl = new RequestLine(rawLine);
        return new HttpRequest(rl, headers, body);
    }
}
