package webserver.http.response;

import webserver.http.common.StatusCode;

public class ResponseBuilder {

    private StatusCode statusCode;
    private byte[] header;
    private byte[] body;
    private String contentType;
    private String redirectUrl;

    public ResponseBuilder(StatusCode statusCode, byte[] body, String contentType) {
        this.statusCode = statusCode;
        this.header = new byte[0];
        this.body = body;
        this.contentType = contentType;
    }

    public ResponseBuilder(StatusCode statusCode, String redirectUrl) {
        this.statusCode = statusCode;
        this.header = new byte[0];
        this.redirectUrl = redirectUrl;
    }

    public Response build() {
       switch (statusCode) {
           case OK, NOT_FOUND, BAD_REQUEST -> writeDefaultMessage();
           case FOUND -> writeRedirectMessage();
       }

        return new Response(header, body);
    }

    private void writeDefaultMessage() {
        String headers = "HTTP/1.1" + statusCode.getCode() + statusCode.getMessage() + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "\r\n";

        header = headers.getBytes();
    }

    private void writeRedirectMessage() {
        String headers = "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getMessage() + "\r\n"
                + "Location: " + redirectUrl + "\r\n"
                + "\r\n";

        header = headers.getBytes();
        body = "".getBytes();
    }
}

