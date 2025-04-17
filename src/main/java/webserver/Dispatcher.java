package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.CookieShop;
import webserver.http.common.HttpHeaders;
import webserver.http.common.HttpMethod;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.StatusLine;
import webserver.mapper.HandlerMapper;
import webserver.resolver.*;
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

            addSessionCookieIfNew(resolveResponse);
            return buildHttpResponse(resolveResponse);
        } catch (HttpException e) {
            logger.error("Error during request processing: {}", e.getMessage());
            ResolveResponse<?> errorResponse = ResolveResponse.status(e.getStatusCode());

            return buildHttpResponse(errorResponse);
        }
    }

    private Resolver determineResolver() {
        String path = request.getPath();
        HttpMethod method = request.getMethod();
        DynamicHandler handler = HandlerMapper.getInstance().getHandler(method, path);

        if (handler == null) {
            logger.debug("Dispatching to ResourceResolver");
            return new ResourceResolver(request);
        }

        logger.debug("Dispatching to DynamicResolver");
        return new DynamicResolver(request, handler);
    }

    private void addSessionCookieIfNew(ResolveResponse<?> resolveResponse) {
        if (request.isNewSession()) {
            HttpHeaders headers = resolveResponse.getHeaders();
            headers.addCookie(request.getSessionId());
        }
    }

    private HttpResponse buildHttpResponse(ResolveResponse<?> responseEntity) {
        return new HttpResponse(
                new StatusLine(responseEntity.getStatusCode()),
                responseEntity.getHeaders(),
                Convertor.convertToByteArray(responseEntity.getBody())
        );
    }

}
