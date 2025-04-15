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
    private Map<String, String> headers = new HashMap<>();

    public HttpRequest(BufferedReader br) throws IOException {
        this.requestLine = br.readLine();
        if  (this.requestLine == null || this.requestLine.isEmpty()) {
            throw new IOException("빈 요청입니다."); //이 에러를 여기서 던지는게 맞는지
        }
        String[] tokens = requestLine.split(" ");
        this.method = tokens[0];
        this.path = tokens[1];
        this.version = tokens[2];

        // 헤더 읽기: 빈 줄이 나올 때까지 반복
        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            int index = headerLine.indexOf(":");
            if (index != -1) {
                String headerName = headerLine.substring(0, index).trim();
                String headerValue = headerLine.substring(index + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

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
