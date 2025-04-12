package webserver.resolver;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.HttpMethod;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;

import static webserver.http.response.HttpStatusCode.METHOD_NOT_ALLOWED;

public class DynamicResolver implements Resolver {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResolver.class);
    private final Controller controller;
    private final HttpRequest request;

    public DynamicResolver(HttpRequest request) {
        this.request = request;
        this.controller = Controller.getInstance();
    }

    @Override
    public ResolveResponse<?> resolve() {
        HttpMethod method = request.getRequestLine().getMethod();
        String path = request.getRequestLine().getPath();

        if (method.equals(HttpMethod.GET) && path.equals("/create")) {
            return controller.getCreate(request);
        } else {
            logger.error("Unsupported method or path: {} {}", method, path);
            throw new HttpException(METHOD_NOT_ALLOWED);
        }
    }

}
