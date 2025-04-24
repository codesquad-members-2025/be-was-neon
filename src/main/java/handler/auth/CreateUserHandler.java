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

public class CreateUserHandler implements Handler {
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);
    @Override
    public Response handle(Request request) {
        Map<String, String> body = request.getBody();

        User user = new User(body.get(USER_ID), body.get(PASSWORD), body.get(NAME), body.get(EMAIL));
        Database.addUser(user);
        logger.debug("create user : {}", user);
        return new Response(HttpStatus.FOUND, SLASH);
    }
}
