package webserver.common;

public enum HttpStatus {
    OK("200", "OK"),
    FOUND("302", "Found"),
    UNAUTHORIZED("401", "Unauthorized"),
    NOT_FOUND("404", "Not Found"),
    NOT_ALLOWED("405", "Not Allowed"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error");

    private String code;
    private String message;

    HttpStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRedirect() {
        return code.startsWith("3");
    }
}
