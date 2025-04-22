package response;

import httpconst.HttpConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentTypeMapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseWriter {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseWriter.class);

    public static void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK" + HttpConst.CRLF);
            dos.writeBytes("Content-Type: " + contentType + HttpConst.CRLF);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + HttpConst.CRLF);
            dos.writeBytes(HttpConst.CRLF);
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

    public static void sendRedirect(DataOutputStream dos, String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found " + HttpConst.CRLF);
        dos.writeBytes("Location: " + location + HttpConst.CRLF);
        dos.writeBytes("Content-Length: 0" + HttpConst.CRLF);
        dos.writeBytes("Connection: keep-alive" + HttpConst.CRLF);
        dos.writeBytes(HttpConst.CRLF);
        dos.flush();
    }

    public static void send405Error(DataOutputStream dos, String allowedMethod) throws IOException {
        dos.writeBytes("HTTP/1.1 405 Method Not Allowed" + HttpConst.CRLF);
        dos.writeBytes("Allow: " + allowedMethod + "\r\n");
        dos.writeBytes("Content-Length: 0" + HttpConst.CRLF);
        dos.writeBytes("Connection: close" + HttpConst.CRLF);
        dos.writeBytes(HttpConst.CRLF);
        dos.flush();
    }

}
