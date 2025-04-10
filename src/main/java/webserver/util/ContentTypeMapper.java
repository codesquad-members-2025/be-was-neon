package webserver.util;

public class ContentTypeMapper {
    public static String getContentType(String path) {
        String lowerPath = path.toLowerCase();

        if (lowerPath.endsWith(".html")) return "text/html;charset=utf-8";
        if (lowerPath.endsWith(".css")) return "text/css;charset=utf-8";
        if (lowerPath.endsWith(".js")) return "application/javascript;charset=utf-8";
        if (lowerPath.endsWith(".json")) return "application/json";
        if (lowerPath.endsWith(".png")) return "image/png";
        if (lowerPath.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (lowerPath.endsWith(".gif")) return "image/gif";
        if (lowerPath.endsWith(".svg")) return "image/svg+xml";
        if (lowerPath.endsWith(".ico")) return "image/x-icon";
        if (lowerPath.endsWith(".xml")) return "application/xml;charset=utf-8";
        return "application/octet-stream"; // 기본값
    }
}
