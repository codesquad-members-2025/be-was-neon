package response.handler;

import Exceptions.HttpException;
import Exceptions.LoginFailedException;
import db.Database;
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

public class LoginHandler implements Handler{

    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        try {
            Map<String, String> params = FormDataParser.parse(request.getRequestBody());
            String userId = params.get("userId");
            String password = params.get("password");

            User user = Database.findUserById(userId)
                    .orElseThrow(()-> new LoginFailedException("User Not Found"));

            if(!user.getPassword().equals(password)) {
                throw new LoginFailedException("Wrong Password");
            }

            Response response = Response.builder()
                    .httpVersion(request.getRequestHeader().getHttpVersion())
                    .status(Status.FOUND)
                    .header(LOCATION, REDIRECT_INDEX_PATH)
                    .header(CONTENT_LENGTH, EMPTY_BODY_LENGTH)
                    .build();

            responseSender.send(response);
        } catch (LoginFailedException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, request, e.getMessage());
        }
    }
}
