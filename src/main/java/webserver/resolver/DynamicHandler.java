package webserver.resolver;

import webserver.http.request.HttpRequest;

@FunctionalInterface
public interface DynamicHandler {
    ResolveResponse<?> handle(HttpRequest request);
}
