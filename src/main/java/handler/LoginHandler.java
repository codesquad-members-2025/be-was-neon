package handler;

import db.Database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;
import webserver.common.HttpStatus;
import webserver.exception.NotRegisteredUserException;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;
import webserver.session.SessionManager;

public class LoginHandler implements Handler{
    @Override
    public Response handle(Request request) {
        byte[] responseBody = new byte[0];

        String userId = request.getBody().get("userId");
        String password = request.getBody().get("password");

        User user = Database.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            throw new NotRegisteredUserException("회원가입하지 않은 사용자입니다.");
        }

        String sessionId = request.getCookie().get("sessionId");
        Session session = SessionManager.getInstance().getSession(sessionId);

        session.setAttribute("sessionUser", user);
        List<String> cookies = List.of(session.getSessionId());

        return new Response(HttpStatus.FOUND, responseBody, "/", cookies);
    }
}
