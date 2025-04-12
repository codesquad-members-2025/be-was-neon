package webserver.http.response;

import static webserver.http.common.HttpConstants.SPACE;

public class StatusLine {

    private static final String httpVersion = "HTTP/1.1";
    private HttpStatusCode httpStatusCode;

    public StatusLine(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return httpVersion + SPACE + httpStatusCode;
    }

}
