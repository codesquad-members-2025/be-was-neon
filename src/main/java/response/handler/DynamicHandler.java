package response.handler;

import common.StatusCode;
import common.UrlPattern;
import db.Database;
import model.User;
import request.Request;
import response.Response;
import response.ResponseWriter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DynamicHandler implements Handler {

    @Override
    public Response handle(Request request) {
        String path = request.getRequestLine("path");
        Map<String, String> queryMap = request.getQueryMap();
        ResponseWriter responseWriter = new ResponseWriter(StatusCode.FOUND, "/index.html");

        if (path.equals(UrlPattern.CREATE_USER.getPattern())) {
            createUser(queryMap);
        }

        return responseWriter.write();
    }

    private void createUser(Map<String, String> queryMap) {
        String userId = URLDecoder.decode((queryMap.get("userId")), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(queryMap.get("password"), StandardCharsets.UTF_8);
        String name = URLDecoder.decode(queryMap.get("name"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(queryMap.get("email"), StandardCharsets.UTF_8);

        User user = new User(userId, password, name, email);

        Database.addUser(user);
    }

}
