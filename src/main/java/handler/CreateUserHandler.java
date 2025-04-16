package handler;

import static webserver.common.Constants.SLASH;

import db.Database;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;

public class CreateUserHandler implements Handler{
    private static final String USER_ID = "userId";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);
    @Override
    public Response handle(Request request) {
        byte[] responseBody = new byte[0];
        Map<String, String> body = request.getBody();

        User user = new User(body.get(USER_ID), body.get(PASSWORD), body.get(NAME), body.get(EMAIL));
        Database.addUser(user);
        logger.debug("create user : {}", user);
        return new Response(HttpStatus.FOUND,  responseBody, SLASH);
    }
}
