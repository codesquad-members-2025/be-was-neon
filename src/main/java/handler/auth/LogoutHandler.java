package handler.auth;

import static webserver.common.Constants.SLASH;

import handler.Handler;
import webserver.common.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

/**
 * 사용자 로그아웃을 처리하는 핸들러 클래스입니다.
 * 이 핸들러는 사용자의 로그아웃 요청을 처리하고, 세션을 무효화합니다.
 */
public class LogoutHandler implements Handler {
    /**
     * 사용자 로그아웃 요청을 처리합니다.
     * 현재 세션을 무효화하고 메인 페이지로 리다이렉트합니다.
     *
     * @param request 로그아웃 요청
     * @return 메인 페이지로의 리다이렉트 응답
     */
    @Override
    public Response handle(Request request) {

        Session session = getSessionByCookie(request);
        if (session != null)
            session.invalidate();

        return new Response(HttpStatus.FOUND, SLASH);
    }


}
