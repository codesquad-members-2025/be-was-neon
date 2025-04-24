package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class Dispatcher {

    private final HttpRequest httpRequest;

    private final String GET = "GET";
    private final String POST = "POST";

    public Dispatcher(HttpRequest request) {
        this.httpRequest = request;
    }

    public HttpResponse dispatch() {
        if (httpRequest.getMethod().equals(GET)) {
            return handleGetRequest();
        } else if (httpRequest.getMethod().equals(POST)) {
            return handlePostRequest();
        } else {
            NotFoundHandler notFoundHandler = new NotFoundHandler(httpRequest);
            return notFoundHandler.createNotFoundResponse();
        }
    }

    private HttpResponse handleGetRequest() {
        GetRequestHandler getHandler = new GetRequestHandler(httpRequest);
        return getHandler.handle();
    }

    private HttpResponse handlePostRequest() {
        PostRequestHandler postHandler = new PostRequestHandler(httpRequest);
        return postHandler.handle();
    }
}
