package request;

public class Request {

    private final RequestStatusLine statusLine;
    private final RequestHeader header;
    private final String bodyContents;

    public Request(RequestStatusLine statusLine, RequestHeader header, String bodyContents) {
        this.statusLine = statusLine;
        this.header = header;
        this.bodyContents = bodyContents;
    }

    public RequestStatusLine getStatusLine() {
        return statusLine;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public String getBodyContents() {
        return bodyContents;
    }
}
