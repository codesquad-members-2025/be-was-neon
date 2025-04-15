package webserver.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;

public class DynamicResolver implements Resolver {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResolver.class);
    private final HttpRequest request;
    private final DynamicHandler dynamicHandler;

    public DynamicResolver(HttpRequest request, DynamicHandler handler) {
        this.request = request;
        this.dynamicHandler = handler;
    }

    @Override
    public ResolveResponse<?> resolve() {
        logger.debug("DynamicResolver");
        return dynamicHandler.handle(request);
    }

}
