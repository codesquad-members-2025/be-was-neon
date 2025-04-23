package handler;

import static webserver.common.Constants.EMPTY;

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
    private final TemplateRenderer renderer;
    private final boolean requireAuth;

    public TemplateHandler(ResourceLoader resourceLoader, TemplateRenderer renderer, boolean requireAuth) {
        this.resourceLoader = resourceLoader;
        this.renderer = renderer;
        this.requireAuth = requireAuth;
    }

    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes(request.getRequestUrl(), true);
        Session session = getSessionByCookie(request);
        User user = (User) session.getAttribute(SESSION_USER);

        if (requireAuth && user == null) {
            throw new UnauthorizedUserException("로그인하지 않은 사용자 입니다.");
        }

        responseBody = renderer.render(user, responseBody);
        return new Response(HttpStatus.OK, responseBody, EMPTY);
    }
}
