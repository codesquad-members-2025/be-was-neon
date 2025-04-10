package webserver.util;

public class ContentTypeMapper {
    public static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html;charset=utf-8";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        return "application/octet-stream"; // 기본값
    }
}
