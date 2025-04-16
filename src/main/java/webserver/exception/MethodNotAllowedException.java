package webserver.exception;

import webserver.common.HttpStatus;

public class MethodNotAllowedException extends RuntimeException {
    private final HttpStatus status;
    public MethodNotAllowedException(String message) {
        super(message);
        this.status = HttpStatus.NOT_ALLOWED;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
