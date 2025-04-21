package handler;

import db.Database;
import httpconst.HttpConst;
import model.User;
import request.Request;
import request.RequestStatusLine;
import response.HttpResponseWriter;
import utils.RequestParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class UserRequestHandler {

    public static void handle(Request request, DataOutputStream dos) throws IOException {
        String bodyContents = request.getBodyContents();

        //body 파싱

        User newUser = new User(
                paramMap.get("userId"),
                paramMap.get("name"),
                paramMap.get("password"),
                paramMap.get("email")
        );
        Database.addUser(newUser);

        // redirect 생각
        HttpResponseWriter.sendRedirect(dos, HttpConst.MAIN_PAGE);
    }

}
