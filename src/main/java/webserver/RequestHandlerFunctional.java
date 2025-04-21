package webserver;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface RequestHandlerFunctional {
    public void handle(HttpRequest request, HttpResponse response) throws IOException;
}
