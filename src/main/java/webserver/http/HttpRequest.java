package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String requestLine;
    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final Map<String, String> parameters;
    private final String body;

    public HttpRequest(String requestLine, String method, String path, String version, Map<String, String> headers, Map<String, String> parameters, String body) {
        this.requestLine = requestLine;
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.parameters = parameters;
        this.body = body;
    }

    public String getPath() {
        return path;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public String getMethod() {
        return method;
    }


    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getBody() {
        return body;
    }

    public String getParam(String key) {
        return parameters.get(key);
    }

    public String getContentType() {
        return headers.get("Content-Type");
    }

    public boolean isFormUrlEncoded() {
        // HTTP 스펙상 Content-Type에는 ';'으로 추가 속성이 붙을 수 있으므로 startWith() 사용
        return getContentType().startsWith("application/x-www-form-urlencoded");
    }

    public Map<String, String> getHeadersForLog() {
        Map<String, String> HeadersForLog = new HashMap<>();
        if (headers.containsKey("Host")) {
            HeadersForLog.put("Host", headers.get("Host"));
        }

        if (headers.containsKey("User-Agent")) {
            HeadersForLog.put("User-Agent", headers.get("User-Agent"));
        }
        if (headers.containsKey("Accept")) {
            HeadersForLog.put("Accept", headers.get("Accept"));
        }
        if (headers.containsKey("Cookie")) {
            HeadersForLog.put("Cookie", headers.get("Cookie"));
        }
        return HeadersForLog;
    }
}
