package handler.view;

import static webserver.common.Constants.EMPTY;

import handler.Handler;
import java.util.List;
import model.User;
import template.TemplateRenderer;
import webserver.common.HttpStatus;
import webserver.exception.UnauthorizedUserException;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

/**
 * 템플릿 기반의 HTTP 요청을 처리하는 핸들러 클래스입니다.
 * 이 핸들러는 템플릿 파일을 로드하고, 사용자의 인증 상태를 확인한 후,
 * 적절한 렌더러를 사용하여 템플릿을 렌더링합니다.
 */

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
    /**
     * HTTP 요청을 처리합니다.
     * 요청된 URL의 템플릿을 로드하고, 인증이 필요한 경우 사용자의 로그인 상태를 확인합니다.
     * 템플릿을 렌더링하여 응답을 생성합니다.
     *
     * @param request 처리할 HTTP 요청
     * @return 렌더링된 템플릿을 포함한 HTTP 응답
     * @throws UnauthorizedUserException 인증이 필요한 페이지에 로그인하지 않은 사용자가 접근한 경우
     */

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
