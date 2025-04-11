package response;

import db.Database;
import model.User;
import request.RequestHeader;
import utils.QueryStringParser;

import java.io.IOException;
import java.util.Map;

public class CreateUserHandler {
    public void sendResponse(RequestHeader requestHeader, ResponseBuilder responseBuilder) throws IOException {
        String path = requestHeader.getPath();
        String queryString = path.substring(path.lastIndexOf('?') + 1).strip();
        Map<String, String> params = QueryStringParser.parse(queryString);
        String userId = params.get("userId");
        String nickname = params.get("nickname");
        String password = params.get("password");
        String email = params.get("email");

        User user = new User(userId, nickname, password, email);
        Database.addUser(user);

        responseBuilder.sendRedirect("/index.html");
    }
}
