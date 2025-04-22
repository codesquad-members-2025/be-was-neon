package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpResponse;
import webserver.http.HttpRequest;
import webserver.util.FormBodyParser;

import java.io.IOException;
import java.util.Map;

public class UserCreateHandler implements Handler{
    private static final Logger log = LoggerFactory.getLogger(UserCreateHandler.class);
    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException{
       if (!request.getMethod().equals("POST")) {
           log.warn("잘못된 요청 방식: {}", request.getMethod());
           response.sendResponse(405,"Method Not Allowed", "text/plain", "POST만 지원합니다.".getBytes());
           return;
       }

        Map<String, String> body;
       if (request.isFormUrlEncoded()) {
           body = FormBodyParser.parseFormBody(request.getBody());
       } else {
           log.warn("지원하지 않는 Content-Type: {}", request.getHeaders().get("Content-Type"));
           response.sendResponse(400, "Bad Request", "text/plain", "지원하지 않는 Content-Type입니다.".getBytes());
           return;
       }

        User user = new User(
                body.get("userId"),
                body.get("password"),
                body.get("username"),
                body.get("email")
        );

        if (Database.findUserById(user.getUserId()) != null) {
            log.warn("회원가입 실패: 중복된 ID {}", user.getUserId());
            response.sendRedirect("/index.html");
            return;
        }

        Database.addUser(user);
        log.info("회원가입: {}", user);

        response.sendRedirect("/index.html");
    }
}
