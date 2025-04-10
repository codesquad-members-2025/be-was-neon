package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseBuilder {
    private final DataOutputStream dos;
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);
    private static final String CRLF = "\r\n";

    public ResponseBuilder(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void sendResponse(byte[] body) {
        response200Header(dos, body.length);
        responseBody(dos, body);
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK " + CRLF);
            dos.writeBytes("Content-Type: text/html;charset=utf-8" + CRLF);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + CRLF);
            dos.writeBytes(CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
