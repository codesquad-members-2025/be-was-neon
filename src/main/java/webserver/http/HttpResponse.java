package webserver.http;

import webserver.util.ContentType;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void sendResponse(int statusCode, String statusMessage, String contentType, byte[] body) throws IOException {
        responseHeader(statusCode, statusMessage, contentType, body);
        responseBody(body);
    }

    public void responseHeader(int statusCode, String statusMessage, String contentType, byte[] body) throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
    }
    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    //200 OK 응답 전송
    public void send200Response(byte[] body, String path) throws IOException {
        String contentType = ContentType.getContentType(path);
        sendResponse(200, "OK", contentType, body);
    }

    public void sendRedirect(String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        dos.writeBytes("Content-Length: 0\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }


}
