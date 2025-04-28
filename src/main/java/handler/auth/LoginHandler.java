package handler.auth;

import static webserver.common.Constants.BLANK;
import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.EQUAL;
import static webserver.common.Constants.HEADER_COOKIE;
import static webserver.common.Constants.PASSWORD;
import static webserver.common.Constants.SEMI_COLON;
import static webserver.common.Constants.SLASH;
import static webserver.common.Constants.USER_ID;

import db.Database;
import handler.Handler;
import model.User;
import webserver.common.HttpStatus;
import webserver.exception.NotRegisteredUserException;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

/**
 * 사용자 로그인을 처리하는 핸들러 클래스입니다.
 * 이 핸들러는 사용자의 로그인 요청을 처리하고, 성공 시 세션을 생성합니다.
 */
public class LoginHandler implements Handler {
    private static final String NOT_REGISTERED_USER = "아이디 또는 비밀번호가 올바르지 않습니다.";
    private static final String ROOT_PATH = "Path=/";

    /**
     * 사용자 로그인 요청을 처리합니다.
     * 요청 본문에서 사용자 정보를 추출하여 인증하고, 성공 시 세션을 생성합니다.
     *
     * @param request 로그인 요청
     * @return 메인 페이지로의 리다이렉트 응답과 세션 쿠키
     * @throws NotRegisteredUserException 아이디 또는 비밀번호가 올바르지 않은 경우
     */
    @Override
    public Response handle(Request request) {

        String userId = request.getBody().get(USER_ID);
        String password = request.getBody().get(PASSWORD);

        User user = Database.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            throw new NotRegisteredUserException(NOT_REGISTERED_USER);
        }

        Session session = getSessionByCookie(request);

        session.setAttribute(SESSION_USER, user);
        String cookies = SESSION_ID + EQUAL +session.getSessionId() + SEMI_COLON + BLANK + ROOT_PATH;

        return new Response(HttpStatus.FOUND, SLASH, cookies);
    }
}
