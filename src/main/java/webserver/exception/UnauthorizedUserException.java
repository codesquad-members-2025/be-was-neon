package webserver.exception;

import webserver.common.HttpStatus;

public class UnauthorizedUserException extends HttpException {
    public UnauthorizedUserException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
