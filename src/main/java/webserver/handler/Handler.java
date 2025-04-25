package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public interface Handler {

    HttpResponse handle(HttpRequest request);
}
