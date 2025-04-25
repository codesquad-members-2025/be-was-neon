package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.exception.MethodNotAllowedException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.session.Session;
import webserver.session.SessionManager;
import webserver.util.FormBodyParser;

import java.io.IOException;
import java.util.Map;

public class UserLoginHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(UserLoginHandler.class);
    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        //todo 회원가입이랑 중복 로직.. -> 분리할까?
        //todo 회원가입도 그렇고 handler가 너무 큰것 같은데 메서드로 분리할까?
        if (!request.getMethod().equals("POST")) {
            log.warn("로그인 - 잘못된 요청 방식: {}", request.getMethod());
            throw new MethodNotAllowedException();
        }

        Map<String, String> body;
        if (request.isFormUrlEncoded()) {
            body = FormBodyParser.parseFormBody(request.getBody());
        } else {
            log.warn("지원하지 않는 Content-Type: {}", request.getHeaders().get("Content-Type"));
            throw new BadRequestException();
        }

        String userId = body.get("userId");
        String password = body.get("password");

        User user = Database.findUserById(userId);
        if (user == null) {
            response.sendRedirect("/index.html?message=no_user");
            return;
        }

        if (!user.getPassword().equals(password)) {
            response.sendRedirect("/login/index.html?error=pw");
            return;
        }

        //세션 생성
        Session session = SessionManager.getInstance().createSession();
        session.setAttribute("user", user);

        //응답에 쿠키 추가
        response.addHeader("Set-Cookie", "SID=" + session.getSessionId() + "; Path=/; HttpOnly");

        //로그인 성공 → 홈으로 리다이렉트
        response.sendRedirect("/index.html");
    }
}
