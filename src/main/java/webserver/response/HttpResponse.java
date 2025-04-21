package webserver.response;

public class HttpResponse {
    private final int status;
    private final String contentType;
    private final byte[] body;
    private final String redirectLocation;

    private static final String PLAIN_TEXT_TYPE = "text/plain";

    private HttpResponse(int status, String contentType, byte[] body, String redirectLocation) {
        this.status = status;
        this.contentType = contentType;
        this.body = body;
        this.redirectLocation = redirectLocation;
    }

    public static HttpResponse ok(String contentType, byte[] body) {
        return new HttpResponse(200, contentType, body, null);
    }

    public static HttpResponse redirect(String location) {
        return new HttpResponse(302, null, null, location);
    }

    public static HttpResponse notFound() {
        return new HttpResponse(404, PLAIN_TEXT_TYPE, "Not Found".getBytes(), null);
    }
}
