package router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.handler.HttpRequestHandler;
import router.handler.impl.MethodNotAllowHandler;
import router.handler.impl.StaticFileHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class Router {

    private final RouteRegistry routeRegistry;
    private final HttpRequestHandler staticFileHandler;

    public Router() {
        this.routeRegistry = RouteRegistry.getInstance();
        this.staticFileHandler = new StaticFileHandler();
    }

    public HttpRequestHandler resolveHandler(HttpRequest request) {
        String path = request.getPath();
        String method = request.getMethod();


        if (routeRegistry.hasRoutesForPath(path)) {
            if (!routeRegistry.isMethodAllowed(method, path)) {
                return new MethodNotAllowHandler(routeRegistry.getAllowedMethods(path));
            }

            HttpRequestHandler handler = routeRegistry.findHandler(method, path);
            if (handler != null) {
                return handler;
            }
        }
            return staticFileHandler;
    }
}


