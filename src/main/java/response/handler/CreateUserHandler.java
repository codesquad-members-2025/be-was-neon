package response.handler;

import db.Database;
import model.User;
import request.Request;
import response.ResponseBuilder;
import utils.FormDataParser;

import java.io.IOException;
import java.util.Map;

public class CreateUserHandler implements Handler {
    @Override
    public void sendResponse(Request request, ResponseBuilder responseBuilder) throws IOException {
        String path = request.getRequestHeader().getPath();
        Map<String, String> params = FormDataParser.parse(request.getRequestBody());
        String userId = params.get("userId");
        String nickname = params.get("nickname");
        String password = params.get("password");
        String email = params.get("email");

        User user = new User(userId, nickname, password, email);
        Database.addUser(user);

        responseBuilder.sendRedirect("/index.html");
    }
}
