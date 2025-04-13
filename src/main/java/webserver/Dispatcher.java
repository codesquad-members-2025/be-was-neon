package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.StatusLine;
import webserver.resolver.DynamicResolver;
import webserver.resolver.ResolveResponse;
import webserver.resolver.Resolver;
import webserver.resolver.ResourceResolver;
import webserver.util.Convertor;

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

            return buildHttpResponse(resolveResponse);
        } catch (HttpException e) {
            logger.error("Error during request processing: {}", e.getMessage());
            ResolveResponse<?> errorResponse = ResolveResponse.status(e.getStatusCode());

            return buildHttpResponse(errorResponse);
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

    private HttpResponse buildHttpResponse(ResolveResponse<?> responseEntity) {
        return new HttpResponse(
                new StatusLine(responseEntity.getStatusCode()),
                responseEntity.getHeaders(),
                Convertor.convertToByteArray(responseEntity.getBody())
        );
    }

}
