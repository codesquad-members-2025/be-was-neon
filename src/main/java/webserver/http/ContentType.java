package webserver.http;

import java.util.HashMap;
import java.util.Map;

public enum ContentType {
    HTML("html", "text/html; charset=UTF-8"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    ICO("ico", "image/x-icon");

    private final String extension;
    private final String mimeType;

    private static final Map<String, ContentType> lookup = new HashMap<>();

    static {
        for (ContentType ct : ContentType.values()) {
            lookup.put(ct.extension, ct);
        }
    }

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static ContentType fromFileName(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1 || lastDot == fileName.length() - 1) {
            return null;
        }

        String ext = fileName.substring(lastDot + 1).toLowerCase();
        return lookup.get(ext);
    }
}
