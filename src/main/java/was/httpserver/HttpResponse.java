package was.httpserver;

import java.io.OutputStream;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpResponse {
    /*
        * HTTP/1.1 404 Not Found
        * Content-Type: text/html; charset=UTF-8
        * Content-Length: 42
        * <h1>페이지를 찾을 수 없습니다.</h1>
     */
    private final PrintWriter writer;
    private final OutputStream outputStream;
    private int statusCode = 200;
    private final StringBuilder bodyBuilder = new StringBuilder();
    private String contentType = "text/html; charset=UTF-8";

    public HttpResponse(PrintWriter writer, OutputStream outputStream) {
        this.writer = writer;
        this.outputStream = outputStream;
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void writeBody(String body) {
        bodyBuilder.append(body);
    }

    public void flush() {
        int contentLength = bodyBuilder.toString().getBytes(UTF_8).length;
        writer.println("HTTP/1.1 " + statusCode + " " + getReasonPhrase(statusCode));
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + contentLength);
        writer.println();
        writer.println(bodyBuilder);
        writer.flush();
    }

    public void flushBinary(byte[] content, String contentType, int statusCode) {
        try {
            writer.println("HTTP/1.1 " + statusCode + " " + getReasonPhrase(statusCode));
            writer.println("Content-Type: " + contentType);
            writer.println("Content-Length: " + content.length);
            writer.println();
            writer.flush();

            outputStream.write(content);
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("Flush binary response failed", e);
        }
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
