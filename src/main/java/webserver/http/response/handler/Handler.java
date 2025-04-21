package webserver.http.response.handler;

import webserver.http.request.Request;
import webserver.http.response.Response;

import java.io.IOException;

public interface Handler {
    Response handle(Request request) throws IOException;
}
