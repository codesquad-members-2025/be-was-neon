package webserver.response;

import webserver.common.HttpStatus;

public class Response {
    private final HttpStatus httpStatus;
    private final byte[] body;
    private final String redirectPath;

    public Response(HttpStatus httpStatus, byte[] body, String redirectPath) {
        this.httpStatus = httpStatus;
        this.body = body;
        this.redirectPath = redirectPath;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public byte[] getBody() {
        return body;
    }

    public String getRedirectPath() {
        return redirectPath;
    }
}
