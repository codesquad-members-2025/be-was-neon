package util;

public enum ContentType {
    HTML("text/html;charset=utf-8"),
    CSS("text/css;charset=utf-8"),
    JS("application/javascript;charset=utf-8"),
    ICO("image/x-icon"),
    PNG("image/png"),
    IMAGE("image/jpeg"),
    SVG("image/svg+xml");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
