package handler.errorHandler;

import static webserver.common.Constants.EMPTY;

import handler.Handler;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

public class MethodNotAllowedHandler implements Handler {
    private final ResourceLoader resourceLoader;

    public MethodNotAllowedHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes("/error/405.html");
        return new Response(HttpStatus.NOT_ALLOWED,  responseBody, EMPTY);
    }
}
