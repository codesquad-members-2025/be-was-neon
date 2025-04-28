package webserver.http;

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
    private Status status = Status.OK;
    private final ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream();
    private String contentType = "text/html; charset=UTF-8";
    private final Map<String, String> headers = new HashMap<>();
    private final static String CRLF = "\r\n";

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public HttpResponse status(Status status) {
        this.status = status;
        return this;
    }

    public HttpResponse contentType(ContentType contentType) {
        this.contentType = contentType.getMimeType();
        return this;
    }

    public HttpResponse header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpResponse body(byte[] body) throws IOException {
        this.bodyBuffer.write(body);
        return this;
    }

    public HttpResponse body(String body) throws IOException {
        return body(body.getBytes(StandardCharsets.UTF_8));
    }

    public void flush() throws IOException {
        byte[] content = bodyBuffer.toByteArray();

        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(String.format(
                "HTTP/1.1 %d %s", status.getCode(), status.getReasonPhrase()+CRLF
        ));

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerBuilder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(CRLF);
        }

        headerBuilder.append("Content-Type: ").append(contentType).append(CRLF);
        headerBuilder.append("Content-Length: ").append(content.length).append(CRLF);
        headerBuilder.append(CRLF);

        outputStream.write(headerBuilder.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.write(content);
        outputStream.flush();
    }
}
