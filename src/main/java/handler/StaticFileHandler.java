package handler;

import static webserver.common.Constants.EMPTY;

import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

public class StaticFileHandler implements Handler {
    private final ResourceLoader resourceLoader;
    public StaticFileHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody;
        responseBody = resourceLoader.fileToBytes(request.getRequestUrl());
        return new Response(HttpStatus.OK, responseBody, EMPTY);
    }
}
