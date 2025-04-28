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

        Map<String, String> body;
        if (request.isFormUrlEncoded()) {
            body = FormBodyParser.parseFormBody(request.getBody());
        } else {
            log.warn("지원하지 않는 Content-Type: {}", request.getHeaders().get("Content-Type"));
            throw new BadRequestException();
        }

        String userId = body.get("userId");
        String password = body.get("password");

        //비즈니스 로직끼리 묶어놓는게 좋을 듯 -> 서비스 패키지로 나누기
        //response 보내주는 클래스로 분리하면 좋을 듯
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
