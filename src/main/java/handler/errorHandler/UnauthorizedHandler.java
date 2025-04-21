package handler.errorHandler;

import static webserver.common.Constants.EMPTY;

import handler.Handler;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

public class UnauthorizedHandler implements Handler {
    private final ResourceLoader resourceLoader;

    public UnauthorizedHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes("/login/fail.html");
        return new Response(HttpStatus.UNAUTHORIZED,  responseBody, EMPTY);
    }
}
