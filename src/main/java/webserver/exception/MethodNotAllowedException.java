package webserver.exception;

import webserver.http.HttpException;

public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException(String method) {
        super(405, "Method Not Allowed", "<h1>405 Method Not Allowed</h1>");
    }
}
