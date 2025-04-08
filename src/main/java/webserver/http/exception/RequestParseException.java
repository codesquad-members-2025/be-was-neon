package webserver.http.exception;

import java.io.IOException;

public class RequestParseException extends IllegalStateException {
    public RequestParseException(String message) {
        super(message);
    }
}
