package response;

import common.StatusCode;

public class ResponseWriter {

    private StatusCode statusCode;
    private byte[] header;
    private byte[] body;
    private String contentType;
    private String redirectUrl;

    public ResponseWriter(StatusCode statusCode, byte[] body, String contentType) {
        this.statusCode = statusCode;
        this.header = new byte[0];
        this.body = body;
        this.contentType = contentType;
    }

    public ResponseWriter(StatusCode statusCode, byte[] body, String contentType, String redirectUrl) {
        this.statusCode = statusCode;
        this.header = new byte[0];
        this.body = body;
        this.contentType = contentType;
        this.redirectUrl = redirectUrl;
    }

    public Response write() {
       switch (statusCode) {
           case OK, NOT_FOUND -> writeDefaultResponse();
           case FOUND -> writeRedirectResponse();
       }

        return new Response(header, body);
    }

    private void writeDefaultResponse() {
        String headers = "HTTP/1.1" + statusCode.getCode() + statusCode.getMessage() + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "\r\n";

        header = headers.getBytes();
    }

    private void writeRedirectResponse() {
        String headers = "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getMessage() + "\r\n"
                + "Location: " + redirectUrl + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "\r\n";

        header = headers.getBytes();
    }
}

