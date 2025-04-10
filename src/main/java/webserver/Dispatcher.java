package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.resolver.DynamicResolver;
import webserver.resolver.ResourceResolver;

import java.io.IOException;

public class Dispatcher {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private final HttpRequest request;
    private final HttpResponse response;

    public Dispatcher(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    public void dispatch() throws IOException {
        if (request.isResource()) {
            logger.debug("Dispatching to ResourceResolver");
            ResourceResolver resourceResolver = new ResourceResolver(request, response);
            resourceResolver.resolve();
        } else {
            logger.debug("Dispatching to DynamicResolver");
            DynamicResolver dynamicResolver = new DynamicResolver(request, response);
            dynamicResolver.resolve();
        }
    }

}
