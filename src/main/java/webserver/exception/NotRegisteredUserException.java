package webserver.exception;

import webserver.common.HttpStatus;

public class NotRegisteredUserException extends HttpException {
    public NotRegisteredUserException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
