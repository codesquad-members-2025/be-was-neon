package webserver.request;

import webserver.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;

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
//            return handlePostRequest();
        }
        return HttpResponse.notFound(); // 404 기본 처리
    }

    private HttpResponse handleGetRequest() {
        GetRequestHandler getHandler = new GetRequestHandler(httpRequest);
        return getHandler.handle();
    }

//    private HttpResponse handlePostRequest() {
//
//    }
}
