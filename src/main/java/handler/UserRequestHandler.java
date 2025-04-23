package handler;

import db.Database;
import httpconst.HttpConst;
import model.User;
import request.Request;
import response.HttpResponseWriter;
import utils.RequestParser;

import java.io.IOException;
import java.util.Map;

public class UserRequestHandler implements Handler{

    @Override
    public void handle(Request request, HttpResponseWriter responseWriter) throws IOException {
        String bodyContents = request.getBodyContents();
        Map<String, String> bodyInfo = RequestParser.parseBody(bodyContents);
        //body 파싱

        User newUser = new User(
                bodyInfo.get("userId"),
                bodyInfo.get("name"),
                bodyInfo.get("password"),
                bodyInfo.get("email")
        );

        Database.addUser(newUser);

        // redirect 생각
        responseWriter.sendRedirect(HttpConst.MAIN_PAGE);
    }

}
