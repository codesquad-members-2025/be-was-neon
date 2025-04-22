package handler;

import static handler.LoginHandler.SESSION_USER;
import static webserver.common.Constants.EMPTY;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import model.User;
import template.TemplateEngine;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

public class DynamicFileHandler implements Handler{
    private final ResourceLoader resourceLoader;
    public DynamicFileHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes(request.getRequestUrl(), true);
        Session session = getSessionByCookie(request);
        responseBody = TemplateEngine.renderingHeader(session, responseBody);

        return new Response(HttpStatus.OK, responseBody, EMPTY);
    }
}
