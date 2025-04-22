package handler;

import static webserver.common.Constants.EMPTY;

import template.TemplateEngine;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

public class UserListHandler implements Handler{
    private final ResourceLoader resourceLoader;
    public UserListHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes(request.getRequestUrl(), true);
        Session session = getSessionByCookie(request);
        responseBody = TemplateEngine.renderingUserList(session, responseBody);

        return new Response(HttpStatus.OK, responseBody, EMPTY);
    }
}
