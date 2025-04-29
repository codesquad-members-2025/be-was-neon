package handler.auth;

import static webserver.common.Constants.PASSWORD;
import static webserver.common.Constants.SLASH;
import static webserver.common.Constants.USER_ID;

import db.Database;
import handler.Handler;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;

/**
 * 사용자 생성을 처리하는 핸들러 클래스입니다.
 * 이 핸들러는 회원가입 요청을 처리하여 새로운 사용자를 데이터베이스에 등록합니다.
 */
public class CreateUserHandler implements Handler {
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);
    /**
     * 사용자 생성 요청을 처리합니다.
     * 요청 본문에서 사용자 정보를 추출하여 새로운 사용자를 생성하고 데이터베이스에 저장합니다.
     *
     * @param request 사용자 생성 요청
     * @return 메인 페이지로의 리다이렉트 응답
     */
    @Override
    public Response handle(Request request) {
        Map<String, String> body = request.getBody();

        User user = new User(body.get(USER_ID), body.get(PASSWORD), body.get(NAME), body.get(EMAIL));
        Database.addUser(user);
        logger.debug("create user : {}", user);
        return new Response(HttpStatus.FOUND, SLASH);
    }
}
