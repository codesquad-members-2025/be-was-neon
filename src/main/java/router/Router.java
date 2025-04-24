package router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.handler.HttpRequestHandler;
import router.handler.impl.StaticFileHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.util.Set;

public class Router {
    private static final Logger logger = LoggerFactory.getLogger(Router.class);
    private final HttpRequestHandler staticFileHandler;

    public Router() {
        this.staticFileHandler = new StaticFileHandler();
    }

    public void route(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        String method = request.getMethod();

        try {
            if (RequestRoute.ALLOWED_METHODS.containsKey(path)) {
                if (!RequestRoute.isMethodAllowed(method, path)) {
                    Set<String> allowedMethods = RequestRoute.getAllowedMethods(path);
                    String allowHeader = String.join(", ", allowedMethods);
                    response.send405(allowHeader);
                    return;
                }

                RequestRoute route = RequestRoute.findByMethodAndPath(method, path);
                if (route != null) {
                    route.getHandler().handle(request, response);
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
