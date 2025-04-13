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
    JPG("image/jpg", ".jpg"),
    GIF("image/gif", ".gif"),
    JSON("application/json;charset=utf-8", ".json"),
    XML("application/xml;charset=utf-8", ".xml"),
    TEXT("text/plain;charset=utf-8", ".txt"),
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
            if (uri.toLowerCase().endsWith(ct.extensions)) {
                return true;
            }
        }
        return false;
    }

    public static ContentType getContentType(String uri) {
        for (ContentType ct : values()) {
            if (uri.toLowerCase().endsWith(ct.extensions)) {
                return ct;
            }
        }

        return DEFAULT;
    }

}
