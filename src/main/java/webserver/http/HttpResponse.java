package webserver.http;

import webserver.util.ContentType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    //todo 빌더 패턴으로 하면 보기 편리할 듯
    private final DataOutputStream dos;
    private final Map<String, String> addedHeaders= new LinkedHashMap<>(); //순서 보장 위해 사용 - 사실 순서 보장 안해도 되는데 테스트나 로깅할때 내가 보기 편하니까..
    private static final String CRLF = "\r\n";

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void sendResponse(int statusCode, String statusMessage, String contentType, byte[] body) throws IOException {
        responseHeader(statusCode, statusMessage, contentType, body);
        responseBody(body);
    }

    public void addHeader (String key, String value) {
        addedHeaders.put(key, value);
    }

    public void responseHeader(int statusCode, String statusMessage, String contentType, byte[] body) throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusMessage + CRLF);
        dos.writeBytes("Content-Type: " + contentType + CRLF);
        dos.writeBytes("Content-Length: " + body.length + CRLF);
        for (Map.Entry<String, String> entry : addedHeaders.entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + CRLF);
        }
        dos.writeBytes(CRLF);
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
        dos.writeBytes("Location: " + location + CRLF);
        dos.writeBytes("Content-Length: 0\r\n");
        for (Map.Entry<String, String> entry : addedHeaders.entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + CRLF);
        }
        dos.writeBytes(CRLF);
        dos.flush();
    }
}
