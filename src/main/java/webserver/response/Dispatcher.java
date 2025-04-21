package webserver.response;

import webserver.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class Dispatcher {

    private final DataOutputStream dos;
    private final HttpRequest httpRequest;
    private final ResponseWriter responseWriter;

    private final String GET = "GET";
    private final String POST = "POST";

    public Dispatcher(HttpRequest request, OutputStream out) {
        this.dos = new DataOutputStream(out);
        this.responseWriter = new ResponseWriter(dos);
        this.httpRequest = request;
    }

    public HttpResponse dispatch() {
        if (httpRequest.getMethod().equals(GET)) {
            return handleGetRequest();
        } else if (httpRequest.getMethod().equals(POST)) {
            return handlePostRequest();
        }
        return HttpResponse.notFound(); // 404 기본 처리
    }

    private HttpResponse handleGetRequest() {
        GetRequestHandler getHandler = new GetRequestHandler(httpRequest);
        return getHandler.isStaticRequest();
    }

    private HttpResponse handlePostRequest() {

    }
}
