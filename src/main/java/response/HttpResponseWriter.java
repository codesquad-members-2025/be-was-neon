package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentTypeMapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseWriter {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseWriter.class);


    public static void response200Header(DataOutputStream dos, String extension, int lengthOfBodyContent) {
        try {
            String contentType = ContentTypeMapper.getContentType(extension);
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
