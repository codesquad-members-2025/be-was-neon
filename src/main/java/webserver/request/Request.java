package webserver.request;

import static webserver.common.Constants.METHOD_INDEX;
import static webserver.common.Constants.URL_IDX;
import static webserver.common.Constants.VERSION_IDX;

import java.util.List;
import java.util.Map;

public class Request {
    private String httpMethod;
    private String requestUrl;
    private String httpVersion;
    private Map<String, String> queryString;
    private Map<String, List<String>> headers;
    private Map<String, String> body;
    private Map<String, String> cookie;

    public Request(String[] requestLine, Map<String, String> queryString, Map<String, List<String>> headers,
                   Map<String, String> body, Map<String, String> cookie) {
        this.httpMethod = requestLine[METHOD_INDEX];
        this.requestUrl = requestLine[URL_IDX];
        this.httpVersion = requestLine[VERSION_IDX];
        this.queryString = queryString;
        this.headers = headers;
        this.body = body;
        this.cookie = cookie;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public Map<String, String> getCookie() {
        return cookie;
    }
}
