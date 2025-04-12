package webserver.resolver;

import com.sun.net.httpserver.Headers;
import webserver.http.common.ContentType;
import webserver.http.common.HttpHeaders;
import webserver.http.response.HttpStatusCode;

import static webserver.http.response.HttpStatusCode.*;

public class ResolveResponse<T> {
    private final HttpStatusCode statusCode;
    private final HttpHeaders headers;
    private final T body;

    private ResolveResponse(HttpStatusCode statusCode, HttpHeaders headers, T body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }

    public static <T> ResolveResponse<T> ok(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", ContentType.HTML.getMimeType());
        return new ResolveResponse<>(OK, headers, body);
    }

    public static <T> ResolveResponse<T> ok(HttpStatusCode statusCode, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html;charset=utf-8");
        return new ResolveResponse<>(statusCode, headers, body);
    }

    public static <T> ResolveResponse<T> ok(T body, ContentType contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", contentType.getMimeType());
        return new ResolveResponse<>(OK, headers, body);
    }

    public static <T> ResolveResponse<T> badRequest(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", ContentType.HTML.getMimeType());
        return new ResolveResponse<>(BAD_REQUEST, headers, body);
    }

    public static <T> ResolveResponse<T> notFound(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", ContentType.HTML.getMimeType());
        return new ResolveResponse<>(NOT_FOUND, headers, body);
    }

}
