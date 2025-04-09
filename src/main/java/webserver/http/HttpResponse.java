package webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpResponse {

    private static final Logger logger = Logger.getLogger(HttpResponse.class.getName());
    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void sendResponse(int statusCode, String statusText, String contentType, byte[] body) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.severe("Error sending response: " + e.getMessage());
        }
    }

    public void send400() {
        String msg = "<h1>400 Bad Request</h1>";
        sendResponse(400, "Bad Request", "text/html;charset=utf-8", msg.getBytes());
    }

    public void send404() {
        String msg = "<h1>404 Not Found</h1>";
        sendResponse(404, "Not Found", "text/html;charset=utf-8", msg.getBytes());
    }

    public void send415() {
        String msg = "<h1>415 Unsupported Media Type</h1>";
        sendResponse(415, "Unsupported Media Type", "text/html;charset=utf-8", msg.getBytes());
    }

}
