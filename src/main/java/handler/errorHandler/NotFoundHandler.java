package handler.errorHandler;

import static webserver.common.Constants.EMPTY;

import handler.Handler;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

public class NotFoundHandler implements Handler {
    private final ResourceLoader resourceLoader;

    public NotFoundHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes("/error/404.html");
        return new Response(HttpStatus.NOT_FOUND,  responseBody, EMPTY);
    }
}
