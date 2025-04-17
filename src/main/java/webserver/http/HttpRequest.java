package webserver.http;

import java.util.Map;

public class HttpRequest {
    private final String METHOD;
    private final String URL_PATH;
    private final String VERSION;
    private final Map<String, String> headers;
    private final String BODY;

    public HttpRequest(String method, String path, String version, Map<String, String> headers, String body) {
        this.METHOD = method;
        this.URL_PATH = path;
        this.VERSION = version;
        this.headers = headers;
        this.BODY = body;
    }

    public String getMETHOD() {
        return METHOD;
    }

    public String getURL_PATH() {
        return URL_PATH;
    }

    public String getPathWithoutQuery() {
        return getURL_PATH().split("\\?")[0];
    }
}
