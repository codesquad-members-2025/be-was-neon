package webserver.resolver;

import java.util.Map;

/**
 * 파일 확장자에 따라 적절한 Content-Type을 결정하는 클래스입니다.
 * 이 클래스는 정적 파일의 MIME 타입을 결정하는 데 사용됩니다.
 */
public class ContentTypeResolver {
    private static final String EXTENSION_SEPARATOR = "\\.";
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final String DEFAULT_EXTENSION = "html";
    private static final int FILE_SPLIT_LENGTH = 2;
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

    /**
     * URL의 파일 확장자에 따라 적절한 Content-Type을 반환합니다.
     * 만약 확장자가 인식되지 않는 경우 기본 Content-Type을 반환합니다.
     *
     * @param url Content-Type을 결정할 URL
     * @return 해당 파일의 Content-Type
     */
    public static String getContentType(String url) {
        String[] splitUrl = url.split(EXTENSION_SEPARATOR);
        String extension = DEFAULT_EXTENSION;
        if (splitUrl.length == FILE_SPLIT_LENGTH) extension = splitUrl[EXTENSION_IDX];

        return contentTypes.getOrDefault(extension, DEFAULT_CONTENT_TYPE);
    }
}
