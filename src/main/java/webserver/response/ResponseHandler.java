package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    private final HttpResponse response;
    private final DataOutputStream dos;
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private final String CRLF = "\r\n";

    public ResponseHandler(OutputStream out, HttpResponse response) {
        this.dos = new DataOutputStream(out);
        this.response = response;
    }

    public void resolveResponse() {
        try {
//            if(response==null) {
//                send500Error();
//                return;
//            }
            int status = response.getStatus();
            logger.debug("Resolving response with status: {}", status);
            switch (status) {
                case 200 , 404 -> sendBodyResponse(status, response.getContentType(), response.getBody());
                case 302 -> send302Redirect(response.getRedirectLocation());
            }
        } catch (IOException e) {
            logger.error("응답 처리 중 오류 발생", e);
            throw new RuntimeException(e);
        }
    }

    private void sendBodyResponse(int status, String contentType, byte[] body) {
        try {
            dos.writeBytes("HTTP/1.1 " + status + " OK" + CRLF);
            dos.writeBytes("Content-Type: " + contentType + CRLF);
            dos.writeBytes("Content-Length: " + body.length + CRLF);
            dos.writeBytes(CRLF);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void send302Redirect(String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found " + CRLF);
        dos.writeBytes("Location: " + location + CRLF);
        dos.writeBytes(CRLF);
        dos.flush();
    }

    private void send500Error() throws IOException {
        dos.writeBytes("HTTP/1.1 500 Internal Server Error" + CRLF);
        dos.writeBytes("Content-Type: text/plain" + CRLF);
        dos.writeBytes("Content-Length: 0" + CRLF);
        dos.writeBytes(CRLF);
        dos.flush();
    }
}
