package webserver;

import java.util.List;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private String protocol;
    private final Map<String, List<String>> headers;
    private final Map<String, List<String>> parameters;
    private String body;

    public HttpRequest(String method,
                       String path,
                       String protocol,
                       Map<String, List<String>> headers,
                       Map<String, List<String>> parameters,
                       String body) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
        this.parameters = parameters;
        this.body = body;
    }

    public String getPath() {
        return path;
    }
}
