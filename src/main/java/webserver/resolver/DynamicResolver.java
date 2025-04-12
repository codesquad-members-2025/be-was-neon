package webserver.resolver;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.ContentType;
import webserver.http.common.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatusCode;

public class DynamicResolver implements Resolver {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResolver.class);
    private final Controller controller;
    private final HttpRequest request;

    public DynamicResolver(HttpRequest request) {
        this.request = request;
        this.controller = Controller.getInstance();
    }

    @Override
    public HttpResponse resolve() {
        HttpMethod method = request.getRequestLine().getMethod();
        String path = request.getRequestLine().getPath();

        if (method.equals(HttpMethod.GET) && path.equals("/create")) {
            controller.getCreate(request);
        } else {
            logger.error("Unsupported method or path: {} {}", method, path);
            response.sendResponse(HttpStatusCode.BAD_REQUEST, ContentType.HTML, "<h1>Bad Request</h1>".getBytes());
        }
    }

}
