package handler;

import webserver.annotation.RequestMapping;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.ContentType;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;
import webserver.resolver.ResolveResponse;
import webserver.util.QueryStringParser;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static webserver.http.response.HttpStatusCode.BAD_REQUEST;
import static webserver.http.response.HttpStatusCode.CONFLICT;

public class Handler {

    private static final Handler instance = new Handler();
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    private Handler() {
    }

    public static Handler getInstance() {
        return instance;
    }

    @RequestMapping(method = "POST", path = "/create")
    public ResolveResponse<User> createUser(HttpRequest request) {
        logger.debug("getCreate");
        Map<String, String> queryString = request.getRequestLine().getQueryString();
        String userId = queryString.get("userId");
        String name = queryString.get("name");
        String password = queryString.get("password");
        String email = queryString.get("email");

        if (userId == null || name == null || password == null || email == null) {
            logger.debug("userId and name and password and email are null!");
            throw new HttpException(BAD_REQUEST);
        }
        if (Database.findUserById(userId) != null) {
            logger.debug("User already exists: {}", userId);
            throw new HttpException(CONFLICT);
        }

        User user = new User(userId, name, password, email);
        Database.addUser(user);
        logger.debug("User created: {}", user);
        return ResolveResponse.ok(ContentType.JSON, user);
    }

}
