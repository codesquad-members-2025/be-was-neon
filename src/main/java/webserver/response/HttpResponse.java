package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ContentType;
import utils.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    public static final String CRLF = "\r\n";
    public static final String HTTP_1_1 = "HTTP/1.1";

    private final DataOutputStream dos;
    private HttpStatus status;
    private ContentType contentType;
    private final Map<String, List<String>> headers = new HashMap<>();
    private byte[] body;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
        this.status = HttpStatus.OK;
        this.contentType = ContentType.HTML;
    }

    public void sendOk(ContentType contentType, byte[] body) throws IOException {
        status(HttpStatus.OK)
                .contentType(contentType)
                .body(body)
                .send();
    }

    private void send() throws IOException {
        writeStatusLine();
        writeHeaders();
        writeBody();
    }

    private void writeStatusLine() throws IOException {
        dos.writeBytes(String.format(HTTP_1_1 + " %d %s" + CRLF, status.getStatusCode(), status.getReasonPhrase()));
    }

    private void writeHeaders() throws IOException {
        if (contentType != null) {
            dos.writeBytes(String.format("Content-Type: %s" + CRLF, contentType.getMineType()));
        }

        if (body != null) {
            dos.writeBytes(String.format("Content-Length: %d" + CRLF, body.length));
        }

        for (var header : headers.entrySet()) {
            dos.writeBytes(String.format("%s: %s" + CRLF, header.getKey(), String.join(",", header.getValue())));
        }

        dos.writeBytes(CRLF);
    }

    private void writeBody() throws IOException {
        if (body != null && body.length > 0) {
            dos.write(body, 0, body.length);
            dos.flush();
        }
    }

    private HttpResponse status(HttpStatus status) {
        this.status = status;
        return this;
    }

    private HttpResponse contentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    private HttpResponse body(byte[] body) {
        this.body = body;
        return this;
    }

    private HttpResponse addHeaders(String name, String value) {
        headers.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        return this;
    }
}
