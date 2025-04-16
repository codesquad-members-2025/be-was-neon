package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContentType;

import java.io.*;

public class Response {

    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private DataOutputStream dos;
    private String path;

    public Response(OutputStream out, String path) {
        this.dos = new DataOutputStream(out);
        this.path = path;
    }

    public void sendResponse() {

        byte[] body = getFileContent(path);

        if (body.length != 0) {
            String extension = getFileExtension(path);
            ContentType contentType = ContentType.valueOf(extension.toUpperCase());
            response200Header(dos, body, contentType.getContentType());
        } else {
            body = getFileContent("error/404.html");
            response404Header(dos, body);
        }
    }

    private String getFileExtension(String url) {
        String[] tokens = url.split("\\.");
        return tokens[1];
    }

    private byte[] getFileContent(String path) {
        byte[] body = new byte[0];

        try (FileInputStream fis = new FileInputStream("src/main/resources/static/" + path)) {
            body = fis.readAllBytes();

        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage());
        }

        return body;
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

