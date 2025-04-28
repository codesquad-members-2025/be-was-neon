package webserver.http.response;

import webserver.http.common.StatusCode;
import java.util.Optional;

public class ResponseBuilder {

    private final StatusCode statusCode;
    private byte[] header;
    private byte[] body;
    private String contentType;
    private String redirectUrl;
    private final Optional<String> sessionId;

    public ResponseBuilder(StatusCode statusCode, byte[] body, String contentType) {
        this.statusCode = statusCode;
        this.header = new byte[0];
        this.body = body;
        this.contentType = contentType;
        this.sessionId = Optional.empty();
    }

    public ResponseBuilder(StatusCode statusCode, String redirectUrl, Optional<String> sessionId) {
        this.statusCode = statusCode;
        this.header = new byte[0];
        this.redirectUrl = redirectUrl;
        this.sessionId = sessionId;
    }

    public Response build() {
       switch (statusCode) {
           case OK, NOT_FOUND, BAD_REQUEST, UNAUTHORIZED -> writeDefaultMessage();
           case FOUND -> writeRedirectMessage();
       }

        return new Response(header, body);
    }

    private void writeDefaultMessage() {
        String headers = "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getMessage() + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "\r\n";

        header = headers.getBytes();
    }

    private void writeRedirectMessage() {
        String headers = "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getMessage() + "\r\n"
                + "Location: " + redirectUrl + "\r\n";

        if (sessionId.isPresent()) {
            headers += "Set-Cookie: sid=" + sessionId.get() + "; Path=/" + "\r\n";
        }

        headers += "\r\n";

        header = headers.getBytes();
        body = "".getBytes();
    }
}

