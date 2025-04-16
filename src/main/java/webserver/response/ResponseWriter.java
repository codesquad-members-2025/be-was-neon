package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseWriter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);
    private final DataOutputStream dos;
    private final String CRLF = "\r\n";

    public ResponseWriter(DataOutputStream dos) {
        this.dos = dos;
    }

    public void send200(int lengthOfBodyContent, String contentType, byte[] body) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK " + CRLF);
            dos.writeBytes("Content-Type: " + contentType + CRLF);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + CRLF);
            dos.writeBytes(CRLF);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void send302Redirect(String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found " + CRLF);
        dos.writeBytes("Location: " + location + CRLF);
        dos.writeBytes(CRLF);
    }
}
