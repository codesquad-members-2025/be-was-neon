package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.resolver.DynamicResolver;
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
        HttpResponse response = resolver.resolve();

        return response;
    }

    private Resolver determineResolver() {
        if (request.isResourceRequest()) {
            logger.debug("Dispatching to ResourceResolver");
            return new ResourceResolver(request);
        }

        logger.debug("Dispatching to DynamicResolver");
        return new DynamicResolver(request);
    }

}
