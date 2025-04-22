package webserver.exception;

import webserver.http.HttpException;

public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException() {
        super(500, "Internal Server Error", "<h1>500 Internal Server Error</h1>");
    }
}
