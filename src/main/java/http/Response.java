package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContentType;
import util.FileContentUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {

    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private DataOutputStream dos;
    private String path;

    public Response(OutputStream out, String path) {
        this.dos = new DataOutputStream(out);
        this.path = path;
    }

    public void sendResponse() {
        if (path.equals("/")) path = "index.html";

        byte[] body = FileContentUtil.getFileContent(path);

        if (body.length != 0) {
            String extension = FileContentUtil.getFileExtension(path);
            ContentType contentType = ContentType.valueOf(extension.toUpperCase());
            response200Header(dos, body, contentType.getContentType());
        } else {
            body = FileContentUtil.getFileContent("error/404.html");
            response404Header(dos, body);
        }
    }

    private void response200Header(DataOutputStream dos, byte[] body, String contentType) {
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

    private void response404Header(DataOutputStream dos, byte[] body) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: " + ContentType.HTML + "\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

