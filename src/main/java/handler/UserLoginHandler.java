package handler;

import db.Database;
import httpconst.HttpConst;
import model.User;
import request.Request;
import response.HttpResponseWriter;
import utils.RequestParser;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class UserLoginHandler implements Handler{

    @Override
    public void handle(Request request, HttpResponseWriter responseWriter) throws IOException {
        String bodyContents = request.getBodyContents();
        Map<String, String> bodyInfo = RequestParser.parseBody(bodyContents);
        String sessionId = UUID.randomUUID().toString();

        User user = Database.findUserById(bodyInfo.get("userId"));
        if(user != null){
            if(user.getPassword().equals(bodyInfo.get("password"))){
                responseWriter.sendRedirectWithCookie(HttpConst.MAIN_PAGE, sessionId);
            }
            else responseWriter.sendRedirect(HttpConst.LOGIN_FAIL_PAGE);
        }
        else responseWriter.sendRedirect(HttpConst.REGISTRATION_PAGE);
    }
}
