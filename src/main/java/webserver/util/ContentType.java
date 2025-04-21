package webserver.util;

import java.util.HashMap;
import java.util.Map;

public class ContentType {

    private static final Map<String, String> MIME = new HashMap<>();

    static  {
        MIME.put(".html", "text/html; charset=utf-8");
        MIME.put(".css", "text/css; charset=utf-8");
        MIME.put(".js", "application/javascript");
        MIME.put(".png", "image/png");
        MIME.put(".jpg", "image/jpeg");
        MIME.put(".svg", "image/svg+xml");
        MIME.put(".ico", "image/x-icon");
        MIME.put(".txt", "text/plain; charset=utf-8");
    }

    public static String getContentType(String path) {
        for (Map.Entry<String, String> entry : MIME.entrySet()) {
            if (path.endsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "application/octet-stream";
    }
}
