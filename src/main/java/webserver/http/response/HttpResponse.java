package webserver.http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import static webserver.http.common.HttpConstants.*;

public class HttpResponse {

    private static final Logger logger = Logger.getLogger(HttpResponse.class.getName());
    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void sendResponse(int statusCode, String statusText, String contentType, byte[] body) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + SPACE + statusText + CR + LF);
            dos.writeBytes("Content-Type: " + contentType + CR + LF);
            dos.writeBytes("Content-Length: " + body.length + CR + LF);
            dos.writeBytes(CR + LF);
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
