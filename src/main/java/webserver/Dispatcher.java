package webserver;

import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpMethod;
import webserver.exception.MethodNotAllowedException;
import webserver.exception.ResourceNotFoundException;
import webserver.request.Request;
import webserver.resolver.ErrorResolver;
import webserver.resolver.MethodResolver;
import webserver.response.Response;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public Response dispatchRequest(Request request) throws FileNotFoundException {
        try {
            HttpMethod method = HttpMethod.getMethod(request.getHttpMethod());
            return MethodResolver.getHandlerByPath(request.getRequestUrl(), method).handle(request);
        }catch (MethodNotAllowedException | ResourceNotFoundException e){
            return ErrorResolver.getHandlerByStatus(e.getStatus()).handle(request);
        }
    }
}