package response.handler;

import Exceptions.HttpException;
import db.UserDatabase;
import model.User;
import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import utils.FormDataParser;

import java.util.Map;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.HttpHeaders.LOCATION;
import static constants.HttpValues.EMPTY_BODY_LENGTH;
import static constants.HttpValues.REDIRECT_INDEX_PATH;

public class CreateUserHandler implements Handler {
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        Map<String, String> params = FormDataParser.parse(request.getRequestBody());
        String userId = params.get("userId");
        String nickname = params.get("nickname");
        String password = params.get("password");
        String email = params.get("email");

        if(UserDatabase.findUserById(userId).isPresent()) {
            throw new HttpException(Status.CONFLICT, request, "User ID already exists");
        }

        User user = new User(userId, nickname, password, email);
        UserDatabase.addUser(user);

        Response response = Response.builder()
                .httpVersion(request.getRequestHeader().getHttpVersion())
                .status(Status.FOUND)
                .header(LOCATION, REDIRECT_INDEX_PATH)
                .header(CONTENT_LENGTH, EMPTY_BODY_LENGTH)
                .build();

        responseSender.send(response);
    }
}
