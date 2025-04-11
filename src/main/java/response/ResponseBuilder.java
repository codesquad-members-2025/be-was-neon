package response;

import loader.ResourceData;
import loader.StaticResourceLoader;
import request.RequestHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseBuilder {
    private final DataOutputStream dos;
    private static final String CRLF = "\r\n";

    public ResponseBuilder(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void sendStatic(RequestHeader requestHeader) throws IOException {
        StaticResourceLoader staticResourceLoader = new StaticResourceLoader(requestHeader.getPath());
        ResourceData resourceData = staticResourceLoader.loadResourceData();

        byte[] body = resourceData.getInputStream().readAllBytes();
        String contentType = ContentTypeMapper.getContentType(resourceData.getExtension());
        response200Header(body.length, contentType);
        responseBody(body);
    }

    public void sendRedirect(String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found " + CRLF);
        dos.writeBytes("Location: " + location + CRLF);
        dos.writeBytes("Content-Length: 0" + CRLF);
        dos.writeBytes("Connection: keep-alive" + CRLF);
        dos.writeBytes(CRLF);
        dos.flush();
    }

    private void response200Header(int lengthOfBodyContent, String contentType) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK " + CRLF);
        dos.writeBytes("Content-Type: " + contentType + CRLF);
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + CRLF);
        dos.writeBytes("Connection: keep-alive" + CRLF);
        dos.writeBytes(CRLF);
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
