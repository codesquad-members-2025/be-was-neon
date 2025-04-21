package router;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface RequestHandlerFunctional {
    public void handle(HttpRequest request, HttpResponse response) throws IOException;
}
