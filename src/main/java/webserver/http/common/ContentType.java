package webserver.http.common;

public enum ContentType {

    HTML("text/html;charset=utf-8", ".html"),
    HTM("text/html;charset=utf-8", ".htm"),
    CSS("text/css;charset=utf-8", ".css"),
    JS("application/javascript;charset=utf-8", ".js"),
    PNG("image/png", ".png"),
    SVG("image/svg+xml", ".svg"),
    ICO("image/x-icon", ".ico"),
    JPEG("image/jpeg", ".jpeg"),
    JPG("image/jpeg", ".jpg"),
    GIF("image/gif", ".gif"),
    JSON("application/json;charset=utf-8", ".json"),
    XML("application/xml;charset=utf-8", ".xml"),
    DEFAULT("application/octet-stream", ".bin");

    private final String mimeType;
    private final String extensions;

    ContentType(String mimeType, String extensions) {
        this.mimeType = mimeType;
        this.extensions = extensions;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static boolean matches(String uri) {
        for (ContentType ct : values()) {
            if (uri.toLowerCase().endsWith(ct.mimeType)) {
                return true;
            }
        }
        return false;
    }

    public static String getContentType(String uri) {
        for (ContentType ct : values()) {
            if (matches(uri)) {
                return ct.getMimeType();
            }
        }

        return DEFAULT.getMimeType();
    }

}
