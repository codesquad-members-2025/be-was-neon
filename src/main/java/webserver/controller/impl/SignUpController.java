package webserver.controller.impl;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ContentType;
import utils.HttpStatus;
import webserver.controller.Controller;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class SignUpController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);
    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final int FIRST_INDEX = 0;
    public static final String INDEX_HTML = "/index.html";

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, List<String>> parameters = request.getParameters();

        if (!parameters.containsKey(USER_ID) ||
                !parameters.containsKey(PASSWORD) ||
                !parameters.containsKey(NAME) ||
                !parameters.containsKey(EMAIL)) {
            response.send(HttpStatus.BAD_REQUEST,
                    ContentType.HTML,
                    "필수 회원 정보가 누락되었습니다.".getBytes(StandardCharsets.UTF_8));
            return;
        }

        String userId = parameters.get(USER_ID).get(FIRST_INDEX);
        String password = parameters.get(PASSWORD).get(FIRST_INDEX);
        String name = parameters.get(NAME).get(FIRST_INDEX);
        String email = parameters.get(EMAIL).get(FIRST_INDEX);

        logger.debug("sign up request - userId: {}, password: {}, name: {}, email: {}",
                userId, password, name, email);

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        response.sendRedirect(INDEX_HTML);
    }
}
