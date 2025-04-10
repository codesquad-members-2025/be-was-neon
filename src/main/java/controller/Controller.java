package controller;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatusCode;

import java.util.Map;

public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    // @GetMapping("/create")
    public void getCreate(HttpRequest request, HttpResponse response) {
        logger.debug("getCreate");
        Map<String, String> queryString = request.getRequestLine().getQueryString();
        String userId = queryString.get("userId");
        String name = queryString.get("name");
        String password = queryString.get("password");
        String email = queryString.get("email");

        if (userId == null || name == null || password == null || email == null) {
            logger.debug("userId and name and password and email are null!");
            response.sendResponse(HttpStatusCode.BAD_REQUEST, "text/plain", "userId and name and password and email are null!".getBytes());
            return;
        }
        if (Database.findUserById(userId) != null) {
            logger.debug("User already exists: {}", userId);
            response.sendResponse(HttpStatusCode.CONFLICT, "text/plain", "User already exists".getBytes());
            return;
        }

        User user = new User(userId, name, password, email);
        Database.addUser(user);
        logger.debug("User created: {}", user);
        response.sendResponse(HttpStatusCode.CREATED, "text/plain", "User created successfully".getBytes());
    }

}
