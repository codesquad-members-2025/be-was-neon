package webserver.request;

import model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String urlPath;
    private final String version;
    private final Map<String, String> headers;
    private final String body;

    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int VERSION_INDEX = 2;

    public HttpRequest(String[] requestParts, Map<String, String> headers, String body) {
        this.method = requestParts[METHOD_INDEX];
        this.urlPath = requestParts[PATH_INDEX];
        this.version = requestParts[VERSION_INDEX];
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
    public String getBody() {
        return body;
    }

    public String getPathWithoutQuery() {
        return getUrlPath().split("\\?")[0];
    }

}
