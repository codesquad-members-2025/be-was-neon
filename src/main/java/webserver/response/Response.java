package webserver.response;

import java.util.ArrayList;
import java.util.List;
import webserver.common.HttpStatus;

public class Response {
    private final HttpStatus httpStatus;
    private final byte[] body;
    private final String redirectPath;
    private String cookie;

    public Response(HttpStatus httpStatus, byte[] body, String redirectPath) {
        this.httpStatus = httpStatus;
        this.body = body;
        this.redirectPath = redirectPath;
    }
    public Response(HttpStatus httpStatus, byte[] body, String redirectPath, String cookie) {
        this.httpStatus = httpStatus;
        this.body = body;
        this.redirectPath = redirectPath;
        this.cookie = cookie;
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

    public String getCookie() {
        return cookie;
    }
}
