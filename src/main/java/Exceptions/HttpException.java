package Exceptions;

import request.Request;
import response.Status;

import java.util.Optional;

public class HttpException extends RuntimeException {
    private final Status status;
    private final Request request;

    public HttpException(Status status, String message) {
        this(status, null, message);
    }

    public HttpException(Status status, Request request, String message) {
        super(message);
        this.status = status;
        this.request = request;
    }

    public Status getStatus() {
        return status;
    }

    public Optional<Request> getRequest() {
        return Optional.ofNullable(request);
    }
}
