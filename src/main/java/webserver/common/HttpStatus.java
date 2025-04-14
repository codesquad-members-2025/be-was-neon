package webserver.common;

public enum HttpStatus {
    HTTP_200("200", "OK"),
    HTTP_302("302", "Found"),
    HTTP_404("404", "Not Found");

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
