package response.handler;

import Exceptions.HttpException;
import db.UserDao;
import model.User;
import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import session.Session;
import session.SessionManager;
import utils.FormDataParser;

import java.util.Map;

import static constants.HttpHeaders.*;
import static constants.HttpValues.EMPTY_BODY_LENGTH;
import static constants.HttpValues.REDIRECT_INDEX_PATH;

public class LoginHandler implements Handler{

    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        Map<String, String> params = FormDataParser.parse(request.getRequestBody());
        String userId = params.get("userId");
        String password = params.get("password");

        User user = UserDao.findByUserId(userId)
                .orElseThrow(()-> new HttpException(Status.UNAUTHORIZED, request, "User not found"));

        if(!user.getPassword().equals(password)) {
            throw new HttpException(Status.UNAUTHORIZED, request, "Wrong Password");
        }

        Session session = SessionManager.createSession(user);

        Response response = Response.builder()
                .httpVersion(request.getRequestHeader().getHttpVersion())
                .status(Status.FOUND)
                .header(LOCATION, REDIRECT_INDEX_PATH)
                .header(CONTENT_LENGTH, EMPTY_BODY_LENGTH)
                .header(SET_COOKIE, "SESSIONID=" + session.getSessionId() + "; Path=/; HttpOnly")
                .build();

        responseSender.send(response);
    }
}
