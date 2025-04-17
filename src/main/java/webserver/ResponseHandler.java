package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContentType;
import util.FileContentUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    private DataOutputStream dos;
    private String path;

    public ResponseHandler(OutputStream out, String path) {
        this.dos = new DataOutputStream(out);
        this.path = path;
    }

    public void sendResponse() {
        if (path.equals("/")) path = "index.html";

        byte[] body = FileContentUtil.getFileContent(path);

        if (body.length != 0) {
            String extension = FileContentUtil.getExtension(path);
            ContentType contentType = ContentType.from(extension);
            response200Header(body, contentType.getContentType());
        } else {
            body = FileContentUtil.getFileContent("error/404.html");
            response404Header(body, ContentType.HTML.getContentType());
        }
    }

    private void response200Header(byte[] body, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(byte[] body, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

