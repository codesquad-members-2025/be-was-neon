package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public interface Handler {
    void handle(HttpRequest request, HttpResponse response) throws IOException;
}
