package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class ContentType {

    private final Map<String, String> MIME;

    public ContentType() {
        MIME = new HashMap<>();
        MIME.put(".html", "text/html; charset=utf-8");
        MIME.put(".css", "text/css; charset=utf-8");
        MIME.put(".js", "application/javascript");
        MIME.put(".png", "image/png");
        MIME.put(".jpg", "image/jpeg");
        MIME.put(".svg", "image/svg+xml");
        MIME.put(".ico", "image/x-icon");
    }

    public String getContentType(String path) {
        for (Map.Entry<String, String> entry : MIME.entrySet()) {
            if (path.endsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "application/octet-stream";
    }
}
