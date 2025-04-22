package webserver.http.response;

import webserver.common.ContentType;
import util.FileUtils;
import webserver.common.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    public static final String CRLF = "\r\n";
    public static final String HTTP_1_1 = "HTTP/1.1";
    public static final String LOCATION = "Location";

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

    public void send(HttpStatus status, ContentType contentType, byte[] body) throws IOException {
        status(status)
                .contentType(contentType)
                .body(body)
                .send();
    }

    public void sendOk(ContentType contentType, byte[] body) throws IOException {
        status(HttpStatus.OK)
                .contentType(contentType)
                .body(body)
                .send();
    }

    public void sendRedirect(String location) throws IOException {
        status(HttpStatus.FOUND)
                .addHeaders(LOCATION, location)
                .send();
    }
    public void send403() throws IOException {
        byte[] errorBody = FileUtils.readFileBytes("/errors/403.html");
        status(HttpStatus.FORBIDDEN)
                .contentType(ContentType.HTML)
                .body(errorBody)
                .send();
    }
    public void send404() throws IOException {
        byte[] errorBody = FileUtils.readFileBytes("/errors/404.html");
        status(HttpStatus.NOT_FOUND)
                .contentType(ContentType.HTML)
                .body(errorBody)
                .send();
    }

    public void send500() throws IOException {
        byte[] errorBody = FileUtils.readFileBytes("/errors/500.html");
        status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(ContentType.HTML)
                .body(errorBody)
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
