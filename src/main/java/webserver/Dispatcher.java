package webserver;

import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.SLASH;

import db.Database;
import java.io.FileNotFoundException;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

public class Dispatcher {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String USER_ID = "userId";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private final ResourceLoader resourceLoader;

    public Dispatcher(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Response dispatchRequest(Request request) throws FileNotFoundException {
        byte[] responseBody = new byte[0];
        if (request.getHttpMethod().equals(GET)) {
            responseBody = resourceLoader.fileToBytes(request.getRequestUrl());
            return new Response(HttpStatus.HTTP_200, responseBody, EMPTY);
        } else if (request.getHttpMethod().equals(POST)) {
            if (request.getRequestUrl().equals("/user/create")){
                Map<String, String> body = request.getBody();

                User user = new User(body.get(USER_ID), body.get(NAME), body.get(PASSWORD), body.get(EMAIL));
                Database.addUser(user);
                logger.debug("create user : {}", user);
                return new Response(HttpStatus.HTTP_302,  responseBody, SLASH);
            }
        }
        return new Response(HttpStatus.HTTP_404, responseBody, EMPTY);
    }
}
