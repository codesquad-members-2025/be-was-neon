package webserver;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeMapper {

    private static final Map<String, String> contentTypeMap = Map.of(
            ".html", "text/html; charset=utf-8",
            ".css", "text/css",
            ".js", "application/javascript",
            ".ico", "image/x-icon",
            ".png", "image/png",
            ".jpg", "image/jpeg",
            ".jpeg", "image/jpeg",
            ".svg", "image/svg+xml",
            ".json", "application/json",
            ".txt", "text/plain"
    );

    public static String getContentType(String contentType) {
        return contentTypeMap.get(contentType);
    }

}
