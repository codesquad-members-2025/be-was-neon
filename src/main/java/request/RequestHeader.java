package request;

import java.util.Map;
import java.util.Optional;

public class RequestHeader {
    private final String method;
    private final String path;
    private final String httpVersion;
    private final Map<String, String> requestHeaders;

    public RequestHeader(String method, String path, String httpVersion, Map<String, String> requestHeaders) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.requestHeaders = requestHeaders;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Optional<String> getHeaderByKey(String key) {
        return Optional.ofNullable(requestHeaders.get(key.toLowerCase()));
    }

    public boolean containsHeader(String key) {
        return requestHeaders.containsKey(key.toLowerCase());
    }
}
