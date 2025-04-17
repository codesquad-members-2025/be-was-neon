package webserver.exception;

import webserver.common.HttpStatus;

public class ResourceNotFoundException extends HttpException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
