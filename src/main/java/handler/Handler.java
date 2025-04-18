package handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.RequestMapping;
import webserver.http.common.HttpSession;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;
import webserver.resolver.ResolveResponse;
import webserver.util.QueryStringParser;

import java.util.Map;

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
    public ResolveResponse<String> createUser(HttpRequest request) {
        logger.debug("getCreate");
        String body = request.getBody();
        Map<String, String> queryString = QueryStringParser.parse(body);
        String userId = queryString.get("userId");
        String name = queryString.get("name");
        String password = queryString.get("password");
        String email = queryString.get("email");

        validateNotBlank(userId, name, password, email);
        if (Database.findUserById(userId) != null) {
            logger.debug("User already exists: {}", userId);
            throw new HttpException(CONFLICT);
        }

        User user = new User(userId, password, name, email);
        Database.addUser(user);
        logger.debug("User created: {}", user);
        return ResolveResponse.redirect("/");
    }

    @RequestMapping(method = "POST", path = "/login")
    public ResolveResponse<String> login(HttpRequest request, HttpSession session) {
        logger.debug("getLogin");
        String body = request.getBody();
        Map<String, String> queryString = QueryStringParser.parse(body);
        String userId = queryString.get("userId");
        String password = queryString.get("password");
        validateNotBlank(userId, password);
        User user = Database.findUserById(userId);
        if (user == null) {
            logger.debug("User not found: {}", userId);
            throw new HttpException(BAD_REQUEST);
        }
        if (!user.getPassword().equals(password)) {
            logger.debug("Invalid password for user: {}", userId);
            throw new HttpException(BAD_REQUEST);
        }
        session.setAttribute("user", user);
        logger.debug("User logged in: {}", user);
        return ResolveResponse.redirect("/");
    }

    private void validateNotBlank(String... values) {
        for (String value : values) {
            if (value == null || value.isBlank()) {
                logger.debug("userId and name and password and email are null!");
                throw new HttpException(BAD_REQUEST);
            }
        }
    }

}
