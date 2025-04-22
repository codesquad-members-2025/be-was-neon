package webserver.request;

import webserver.response.HttpResponse;

public class PostRequestHandler {
    private final HttpRequest request;

    private final String POST = "POST";

    public PostRequestHandler(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse handle() {
        String path = request.getUrlPath();

        if (path.equals("/create")) {
            return handleUserCreation();
        }
        return HttpResponse.notFound();
    }

    private HttpResponse handleUserCreation() {
        if (!request.getMethod().equals(POST)) return HttpResponse.notFound();


    }
}
