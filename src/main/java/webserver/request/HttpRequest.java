package webserver.request;

import model.User;

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

    public HttpRequest(String method, String path, String version, Map<String, String> headers, String body) {
        this.method = method;
        this.urlPath = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public String getMETHOD() {
        return method;
    }

    public String getURL_PATH() {
        return urlPath;
    }

    public String getPathWithoutQuery() {
        return getURL_PATH().split("\\?")[0];
    }

    public User parseRegistrationData() {
        int index = urlPath.indexOf("?");
        if (index == -1) return null;

        String query = urlPath.substring(index + 1);
        Map<String, String> param = new HashMap<>();

        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);   // "="로 딱 한번만 분리한다
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            param.put(key, value);
        }
        return new User(param.get(USER_ID), param.get(PASSWORD), param.get(NAME), param.get(EMAIL));
    }
}
