package response;

import loader.ResourceData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseBuilder {
    private final DataOutputStream dos;
    private static final String CRLF = "\r\n";

    public ResponseBuilder(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void sendResponse(ResourceData resourceData) throws IOException {
        byte[] body = resourceData.getInputStream().readAllBytes();
        String contentType = ContentTypeMapper.getContentType(resourceData.getExtension());

        response200Header(dos, body.length, contentType);
        responseBody(dos, body);
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK " + CRLF);
        dos.writeBytes("Content-Type: " + contentType + CRLF);
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + CRLF);
        dos.writeBytes(CRLF);
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
