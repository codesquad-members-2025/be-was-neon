package webserver.http.response.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.StatusCode;
import webserver.http.common.UrlPattern;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.ResponseBuilder;

import java.util.Map;

public class DynamicHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(DynamicHandler.class);

    @Override
    public Response handle(Request request) {
        String path = request.getRequestLine("path");
        Map<String, String> queryMap = request.getQueryMap();

        if (path.equals(UrlPattern.CREATE_USER.getPattern())) {
            createUser(queryMap);
        }

        return new ResponseBuilder(StatusCode.FOUND, "/index.html").build();
    }

    private void createUser(Map<String, String> queryMap) {
        String userId = (queryMap.get("userId"));
        String password = (queryMap.get("password"));
        String name = (queryMap.get("name"));
        String email = (queryMap.get("email"));

        User user = new User(userId, password, name, email);

        Database.addUser(user);
    }

}
