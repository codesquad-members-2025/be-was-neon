package webserver.exception;

import webserver.http.HttpException;

public class NotFoundException extends HttpException {
    public NotFoundException() {
        super(404, "Not Found", "<h1>404 Not Found</h1>");
    }
}
