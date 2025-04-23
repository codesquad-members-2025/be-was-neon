package webserver.http.common;

public enum StatusCode {

    OK("200", "OK"),
    NOT_FOUND("404", "Not Found"),
    FOUND("302", "Found"),
    BAD_REQUEST("400", "Bad Request");

    private final String code;
    private final String message;

    StatusCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
