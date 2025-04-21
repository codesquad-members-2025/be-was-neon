package webserver.http.common;

public enum StatusCode {

    OK("200", "OK"),
    NOT_FOUND("404", "Not Found"),
    FOUND("302", "Found");

    private String code;
    private String message;

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
