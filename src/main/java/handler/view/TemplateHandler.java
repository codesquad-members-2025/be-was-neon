package handler.view;

import static webserver.common.Constants.EMPTY;

import handler.Handler;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import model.User;
import template.TemplateRenderer;
import webserver.common.HttpStatus;
import webserver.exception.UnauthorizedUserException;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

public class TemplateHandler implements Handler {
    private final ResourceLoader resourceLoader;
    private final List<TemplateRenderer> renderer;
    private final boolean requireAuth;
    private String viewPath;

    public TemplateHandler(ResourceLoader resourceLoader, List<TemplateRenderer> renderer, boolean requireAuth) {
        this(resourceLoader, renderer, requireAuth, EMPTY);
    }

    public TemplateHandler(ResourceLoader resourceLoader, List<TemplateRenderer> renderer, boolean requireAuth, String viewPath) {
        this.resourceLoader = resourceLoader;
        this.renderer = renderer;
        this.requireAuth = requireAuth;
        this.viewPath = viewPath;
    }

    @Override
    public Response handle(Request request) {
        if (viewPath.equals(EMPTY)) viewPath = request.getRequestUrl();

        byte[] responseBody = resourceLoader.fileToBytes(viewPath, true);
        Session session = getSessionByCookie(request);
        User user = (User) session.getAttribute(SESSION_USER);

        if (requireAuth && user == null) {
            throw new UnauthorizedUserException(NOT_LOGIN_USER);
        }

        for (TemplateRenderer templateRenderer : renderer) {
            responseBody = templateRenderer.render(user, responseBody);
        }

        return new Response(HttpStatus.OK, responseBody, EMPTY);
    }
}
