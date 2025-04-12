package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatusCode;
import webserver.http.response.StatusLine;
import webserver.resolver.DynamicResolver;
import webserver.resolver.ResolveResponse;
import webserver.resolver.Resolver;
import webserver.resolver.ResourceResolver;

import java.io.IOException;

public class Dispatcher {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private final HttpRequest request;

    public Dispatcher(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse dispatch() throws IOException {
        Resolver resolver = determineResolver();
        try {
             ResolveResponse<?> resolveResponse = resolver.resolve();

            return new HttpResponse(
                    new StatusLine(resolveResponse.getStatusCode()),
                    resolveResponse.getHeaders(),
                    (byte[]) resolveResponse.getBody()
            );
        } catch (HttpException e) {
            logger.error("Error during request processing: {}", e.getMessage());
            ResolveResponse<?> response = handleException(e);
            return new HttpResponse(
                    new StatusLine(response.getStatusCode()),
                    response.getHeaders(),
                    (byte[]) response.getBody()
            );
        }
    }

    private Resolver determineResolver() {
        if (request.isResourceRequest()) {
            logger.debug("Dispatching to ResourceResolver");
            return new ResourceResolver(request);
        }

        logger.debug("Dispatching to DynamicResolver");
        return new DynamicResolver(request);
    }

    private ResolveResponse<?> handleException(HttpException e) {
        if (e.getStatusCode() == HttpStatusCode.NOT_FOUND) {
            logger.error("Resource not found: {}", e.getMessage());
            return ResolveResponse.notFound(HttpStatusCode.NOT_FOUND);
        } else if (e.getStatusCode() == HttpStatusCode.UNSUPPORTED_MEDIA_TYPE) {
            logger.error("Unsupported media type: {}", e.getMessage());
            return ResolveResponse.notFound(HttpStatusCode.UNSUPPORTED_MEDIA_TYPE);
        } else {
            logger.error("Bad request: {}", e.getMessage());
            return ResolveResponse.badRequest(HttpStatusCode.BAD_REQUEST);
        }
    }

}
