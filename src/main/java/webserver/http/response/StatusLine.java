package webserver.http.response;

import static webserver.http.common.HttpConstants.SPACE;

public class StatusLine {

    private static final String httpVersion = "HTTP/1.1";
    private final HttpStatusCode httpStatusCode;

    public StatusLine(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return httpVersion + SPACE + httpStatusCode;
    }

}
