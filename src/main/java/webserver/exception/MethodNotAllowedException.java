package webserver.exception;

import webserver.common.HttpStatus;

public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException(String message) {
        super(message, HttpStatus.NOT_ALLOWED);
    }
}
