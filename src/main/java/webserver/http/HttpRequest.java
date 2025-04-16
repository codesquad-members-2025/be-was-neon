package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String requestLine;
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers;

    public HttpRequest(String requestLine, String method, String path, String version, Map<String, String> headers) {
        this.requestLine = requestLine;
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
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
