package webserver.http.response;

import webserver.http.common.HttpHeaders;

import java.util.Arrays;

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

    @Override
    public String toString() {
        return statusLine + CR + LF +
                headers + CR + LF +
                CR + LF +
                Arrays.toString(body) + CR + LF;
    }

}
