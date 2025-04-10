package webserver;

import java.util.Map;

public class ContentTypeResolver {
    private static final String EXTENSION_SEPARATOR = "\\.";
    private static final String DEFAULT_CONTENT_TYPE = "*/*";
    private static final int EXTENSION_IDX = 1;

    private static final Map<String, String> contentTypes = Map.ofEntries(
            Map.entry("html", "text/html;charset=utf-8"),
            Map.entry("css", "text/css"),
            Map.entry("js", "application/javascript"),
            Map.entry("png", "image/png"),
            Map.entry("jpg", "image/jpeg"),
            Map.entry("jpeg", "image/jpeg"),
            Map.entry("ico", "image/x-icon"),
            Map.entry("svg", "image/svg+xml")
    );

    public static String getContentType(String url) {
        String[] splitUrl = url.split(EXTENSION_SEPARATOR);
        String extension = "html";
        if (splitUrl.length == 2) extension = splitUrl[EXTENSION_IDX];

        return contentTypes.getOrDefault(extension, DEFAULT_CONTENT_TYPE);
    }
}
