package webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    /*
     * HTTP/1.1 404 Not Found
     * Content-Type: text/html; charset=UTF-8
     * Content-Length: 42
     * <h1>페이지를 찾을 수 없습니다.</h1>
     */
    private final OutputStream outputStream;
    private int statusCode = 200;
    private final ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream();
    private String contentType = "text/html; charset=UTF-8";
    private final Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void writeBody(byte[] body) throws IOException {
        bodyBuffer.write(body);
    }

    public void flush() throws IOException {
        byte[] content = bodyBuffer.toByteArray();

        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(String.format(
                "HTTP/1.1 %d %s\r\n", statusCode, getReasonPhrase(statusCode)
        ));

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        headerBuilder.append("Content-Type: ").append(contentType).append("\r\n");
        headerBuilder.append("Content-Length: ").append(content.length).append("\r\n");
        headerBuilder.append("\r\n");

        outputStream.write(headerBuilder.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.write(content);
        outputStream.flush();
    }

    private String getReasonPhrase(int statusCode) {
        switch (statusCode) {
            case 200:
                return "OK";
            case 201:
                return "Created";
            case 204:
                return "No Content";
            case 301:
                return "Moved Permanently";
            case 302:
                return "Found";
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 405:
                return "Method Not Allowed";
            case 500:
                return "Internal Server Error";
            case 502:
                return "Bad Gateway";
            case 503:
                return "Service Unavailable";
            default:
                return "Unknown Status";
        }
    }
}
