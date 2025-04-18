package response.handler;

import Exceptions.HttpException;
import db.Database;
import model.User;
import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import utils.FormDataParser;

import java.io.IOException;
import java.util.Map;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.HttpHeaders.LOCATION;

public class CreateUserHandler implements Handler {
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        try {
            String path = request.getRequestHeader().getPath();
            Map<String, String> params = FormDataParser.parse(request.getRequestBody());
            String userId = params.get("userId");
            String nickname = params.get("nickname");
            String password = params.get("password");
            String email = params.get("email");

            User user = new User(userId, nickname, password, email);
            Database.addUser(user);

            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.FOUND)
                    .header(LOCATION, "/index.html")
                    .header(CONTENT_LENGTH, "0")
                    .build();

            responseSender.send(response);
        } catch (IOException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, request, e.getMessage());
        }
    }
}
