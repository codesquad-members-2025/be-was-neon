package webserver.http.response;

import webserver.http.common.ContentType;

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

    public void sendResponse(HttpStatusCode httpStatusCode, ContentType contentType, byte[] body) {
        try {
            dos.writeBytes("HTTP/1.1 " + httpStatusCode.getStatusCode() + SPACE + httpStatusCode.getReasonPhrase() + CR + LF);
            dos.writeBytes("Content-Type: " + contentType.getMimeType() + CR + LF);
            dos.writeBytes("Content-Length: " + body.length + CR + LF);
            dos.writeBytes(CR + LF);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.severe("Error sending response: " + e.getMessage());
        }
    }

}
