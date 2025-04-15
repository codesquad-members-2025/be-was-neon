package webserver.http.response;

import webserver.http.common.HttpHeaders;

import java.nio.charset.StandardCharsets;

import static webserver.http.common.HttpConstants.CR;
import static webserver.http.common.HttpConstants.LF;

public class HttpResponse {

    private final StatusLine statusLine;
    private final HttpHeaders headers;
    private final byte[] body;

    public HttpResponse(StatusLine statusLine, HttpHeaders headers, byte[] body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public byte[] getBytes() {
        String headerPart = statusLine + CR + LF +
                headers + CR + LF;
        byte[] headerBytes = headerPart.getBytes(StandardCharsets.UTF_8);
        byte[] responseBytes = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, responseBytes, 0, headerBytes.length);
        System.arraycopy(body, 0, responseBytes, headerBytes.length, body.length);

        return responseBytes;
    }

}
