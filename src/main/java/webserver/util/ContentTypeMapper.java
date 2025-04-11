package webserver.util;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeMapper {
    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<>();
    static {
        CONTENT_TYPE_MAP.put("html", "text/html;charset=utf-8");
        CONTENT_TYPE_MAP.put("css", "text/css;charset=utf-8");
        CONTENT_TYPE_MAP.put("js", "application/javascript;charset=utf-8");
        CONTENT_TYPE_MAP.put("json", "application/json");
        CONTENT_TYPE_MAP.put("ico", "image/x-icon");
        CONTENT_TYPE_MAP.put("png", "image/png");
        CONTENT_TYPE_MAP.put("jpg", "image/jpeg");
        CONTENT_TYPE_MAP.put("jpeg", "image/jpeg");
        CONTENT_TYPE_MAP.put("gif", "image/gif");
        CONTENT_TYPE_MAP.put("svg", "image/svg+xml");
        CONTENT_TYPE_MAP.put("xml", "application/xml;charset=utf-8");
    }

    public static String getContentType(String path) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == path.length() - 1) {
            return "application/octet-stream"; // 확장자 없음 or .으로 끝남 ( /file. /download. 등)
        }
        String extension = path.substring(dotIndex + 1).toLowerCase();
        return CONTENT_TYPE_MAP.getOrDefault(extension, "application/octet-stream"); //기본값

    }
}
