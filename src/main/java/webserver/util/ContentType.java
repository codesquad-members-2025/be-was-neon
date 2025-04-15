package webserver.util;

public enum ContentType {
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css;charset=utf-8"),
    JS("js", "application/javascript;charset=utf-8"),
    JSON("json", "application/json"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    XML("xml", "application/xml;charset=utf-8");

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

    public static String getContentType(String path) {
        String extension = extractExtension(path);
        return getMimeTypeForExtension(extension);
    }

    private static String extractExtension(String path) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == path.length() - 1) {
            return null;
        }
        return path.substring(dotIndex + 1);
    }

    public static String getMimeTypeForExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return "application/octet-stream";
        }
        for (ContentType type : values()) {
            if (type.extension.equalsIgnoreCase(extension)) {
                return type.mimeType;
            }
        }
        return "application/octet-stream";
    }
}
