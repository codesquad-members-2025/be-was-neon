package webserver;

import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpMethod;
import webserver.request.Request;
import webserver.resolver.MethodResolver;
import webserver.response.Response;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public Response dispatchRequest(Request request) throws FileNotFoundException {
        HttpMethod method = HttpMethod.getMethod(request.getHttpMethod());
        return MethodResolver.getHandlerByPath(request.getRequestUrl(), method).handle(request);
    }
}