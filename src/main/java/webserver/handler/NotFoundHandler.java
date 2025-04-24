package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.Status;
import webserver.util.Constants;

public class NotFoundHandler {
    private final HttpRequest request;

    private final String MESSAGE_404 = "404 Not Found";

    public NotFoundHandler(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse createNotFoundResponse() {
        Status status = Status.NOT_FOUND;
        String message = MESSAGE_404;
        byte[] body = message.getBytes();
        return HttpResponse.getBuilder()
                .httpVersion(Constants.HTTP_VERSION)
                .status(status)
                .header(Constants.CONTENT_TYPE, Constants.PLAIN_TEXT)
                .header(Constants.CONTENT_LENGTH, String.valueOf(body.length))
                .body(body)
                .build();
    }
}
