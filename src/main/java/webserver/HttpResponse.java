package webserver;

import webserver.util.ContentTypeMapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    //200 OK 응답 전송
    public void send200Response(byte[] body, String path) throws IOException {
        String contentType = ContentTypeMapper.getContentType(path);
        dos.writeBytes("HTTP/1.1 200 OK\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        responseBody(body);
    }

    public void send404Response() throws IOException {
        String body = "<h1>404 Not Found</h1>";
        byte[] bytes = body.getBytes();
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + bytes.length + "\r\n");
        dos.writeBytes("\r\n");
        responseBody(bytes);
    }
}
