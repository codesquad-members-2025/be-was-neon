package webserver.resolver;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class DynamicResolver implements Resolver {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResolver.class);
    private static final Controller controller = new Controller();
    private final HttpRequest request;
    private final HttpResponse response;

    public DynamicResolver(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void resolve() {
        HttpMethod method = request.getRequestLine().getMethod();
        String path = request.getRequestLine().getPath();

        if (method.equals(HttpMethod.GET) && path.equals("/create")) {
            controller.getCreate(request, response);
        } else {
            logger.error("Unsupported method or path: {} {}", method, path);
            response.send400();
        }
    }

}
