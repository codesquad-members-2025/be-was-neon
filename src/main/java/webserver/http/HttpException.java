package webserver.http;

import java.io.IOException;

public class HttpException extends Exception {
    private final int statusCode;
    private final String statusMessage;
    private final byte[] body;

    protected HttpException(int statusCode, String statusMessage, String body) {
        super(statusMessage);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.body = body.getBytes();
    }
    public void sendError(HttpResponse res) throws IOException {
        res.sendResponse(statusCode,
                statusMessage,
                "text/html;charset=utf-8",
                body);
    }

}
