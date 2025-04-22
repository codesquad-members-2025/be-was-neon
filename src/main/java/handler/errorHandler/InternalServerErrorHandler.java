package handler.errorHandler;

import static webserver.common.Constants.EMPTY;

import handler.Handler;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

public class InternalServerErrorHandler implements Handler {
    private final ResourceLoader resourceLoader;

    public InternalServerErrorHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes("/error/500.html", false);
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR,  responseBody, EMPTY);
    }
}
