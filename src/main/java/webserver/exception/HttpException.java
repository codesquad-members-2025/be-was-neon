package webserver.exception;

import webserver.common.HttpStatus;

public abstract class HttpException extends RuntimeException {
    private final HttpStatus status;

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}