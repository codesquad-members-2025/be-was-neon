package webserver.util;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeMapper {
    private static final Map<String, String> CONTENT_TYPE_MAP = Map.of(
            "html", "text/html;charset=utf-8",
            "css", "text/css;charset=utf-8",
            "js", "application/javascript;charset=utf-8",
            "json", "application/json",
            "ico", "image/x-icon",
            "png", "image/png",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "gif", "image/gif",
            "xml", "application/xml;charset=utf-8"
    );

    public static String getContentType(String path) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == path.length() - 1) {
            return "text/plain;charset=utf-8"; // 확장자 없음 or .으로 끝남 ( /file. /download. 등)
        }
        String extension = path.substring(dotIndex + 1).toLowerCase();
        return CONTENT_TYPE_MAP.getOrDefault(extension, "application/octet-stream"); //기본값

    }
}
