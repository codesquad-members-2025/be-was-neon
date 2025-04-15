package domain;

public enum ContentType {
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    GIF("gif", "image/gif"),
    ICO("ico", "image/x-icon"),
    WOFF("woff", "font/woff"),
    WOFF2("woff2", "font/woff2"),
    TTF("ttf", "font/ttf"),
    EOT("eot", "application/vnd.ms-fontobject"),
    OTF("otf", "font/otf"),
    DEFAULT("", "application/octet-stream");

    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static String getMimeTypeByExtension(String fileExtension) {
        for (ContentType contentType : values()) {
            if (contentType.getExtension().equalsIgnoreCase(fileExtension)) {
                return contentType.getMimeType();
            }
        }
        return DEFAULT.getMimeType(); // 기본 MIME 타입 반환
    }
}
