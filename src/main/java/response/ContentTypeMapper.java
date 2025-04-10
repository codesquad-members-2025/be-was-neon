package response;

import java.util.Map;

public class ContentTypeMapper {
    private static final Map<String, String> responseType = Map.of(
            "html", "text/html",
            "css", "text/css",
            "js", "application/javascript",
            "json", "application/json",
            "png", "image/png",
            "jpg", "image/jpeg",
            "gif", "image/gif",
            "svg", "image/svg+xml",
            "ico", "image/x-icon"
    );

    public static String getContentType(String extention) {
        return responseType.getOrDefault(extention, "application/octet-stream");
    }
}
