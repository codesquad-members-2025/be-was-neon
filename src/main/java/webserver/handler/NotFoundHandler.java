package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public class NotFoundHandler implements Handler {
    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        response.send404Response();
    }
}
