package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseSender {

    private final DataOutputStream dos;

    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);
    private final int BODY_OFFSET = 0;

    public ResponseSender(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void sendResponse(HttpResponse response) {
        try {
            dos.writeBytes(response.getHeader());
            dos.write(response.getBody(), BODY_OFFSET, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error("failed to send HttpResponse");
        }
    }
}
