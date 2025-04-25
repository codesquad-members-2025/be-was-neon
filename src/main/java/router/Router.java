package router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.handler.HttpRequestHandler;
import router.handler.impl.StaticFileHandler;
import webserver.common.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class Router {

    private static final Logger logger = LoggerFactory.getLogger(Router.class);
    private final RouteRegistry routeRegistry;
    private final HttpRequestHandler staticFileHandler;

    public Router() {
        this.routeRegistry = RouteRegistry.getInstance();
        this.staticFileHandler = new StaticFileHandler();
    }

    public void route(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        String method = request.getMethod();

        try {
            if (routeRegistry.hasRoutesForPath(path)) {
                if (!routeRegistry.isMethodAllowed(method, path)) {
                    Set<HttpMethod> allowedMethods = routeRegistry.getAllowedMethods(path);
                    String allowHeader = allowedMethods.stream().map(Enum::name).collect(Collectors.joining(", "));
                    response.send405(allowHeader); //todo errorHandler로 처리하게 변경
                    return;
                }

                HttpRequestHandler handler = routeRegistry.findHandler(method, path);
                if (handler != null) {
                    handler.handle(request, response);
                    return;
                }
            }

            staticFileHandler.handle(request, response);
        } catch (Exception e) {
            logger.error("요청 처리 중 오류 발생: {}", e.getMessage(), e);
            response.send500();
        }
    }
}
