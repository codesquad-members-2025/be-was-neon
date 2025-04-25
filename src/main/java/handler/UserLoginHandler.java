package handler;

import db.Database;
import httpconst.HttpConst;
import model.User;
import request.Request;
import response.HttpResponseWriter;
import session.Session;
import session.SessionManager;
import utils.RequestParser;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class UserLoginHandler implements Handler{

    @Override
    public void handle(Request request, HttpResponseWriter responseWriter) throws IOException {
        String bodyContents = request.getBodyContents();
        Map<String, String> bodyInfo = RequestParser.parseBody(bodyContents);

        // 세션 생성
        User user = Database.findUserById(bodyInfo.get("userId"));
        Session session = SessionManager.createSession(user);

        if(user != null){
            if(user.getPassword().equals(bodyInfo.get("password"))){
                responseWriter.sendRedirectWithCookie(HttpConst.MAIN_PAGE, session);
                // 세션 넘기기
            }
            else responseWriter.sendRedirect(HttpConst.LOGIN_FAIL_PAGE);
        }
        else responseWriter.sendRedirect(HttpConst.REGISTRATION_PAGE);
    }
}
