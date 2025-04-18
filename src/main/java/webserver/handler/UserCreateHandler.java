package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpResponse;
import webserver.http.HttpRequest;

import java.io.IOException;
import java.util.Map;

public class UserCreateHandler implements Handler{
    private static final Logger log = LoggerFactory.getLogger(UserCreateHandler.class);
    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException{
        Map<String, String> params = request.getParameters();
        User user = new User(
                params.get("userId"),
                params.get("username"),
                params.get("password"),
                params.get("email")

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
