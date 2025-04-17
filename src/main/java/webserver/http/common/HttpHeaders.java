package webserver.http.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {

    private static final Logger logger = LoggerFactory.getLogger(HttpHeaders.class.getName());
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String LOCATION = "Location";
    private final Map<String, String> headers;

    public HttpHeaders() {
        this.headers = new HashMap<>();
    }

    public void add(String key, String value) {
        headers.put(key, value);
    }

    public void addContentType(ContentType contentType) {
        headers.put(CONTENT_TYPE, contentType.getMimeType());
    }

    public void addLocation(String location) {
        headers.put(LOCATION, location);
    }

    public boolean containsKey(String key) {
        return headers.containsKey(key);
    }

    public String get(String fieldName) {
        if (!headers.containsKey(fieldName)) {
            logger.error("Header에 {}이 존재하지않습니다.", fieldName);
            return null;
        }

        return headers.get(fieldName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        return sb.toString();
    }

}
