package webserver.exception;

import webserver.common.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {
    private final HttpStatus status;
    public ResourceNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
