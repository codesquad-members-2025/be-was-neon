package webserver.handler;

import db.Database;
import model.User;
import webserver.http.HttpResponse;
import webserver.http.HttpRequest;

import java.io.IOException;
import java.util.Map;

public class UserCreateHandler implements Handler{
    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException{
        Map<String, String> params = request.getParameters();
        User user = new User(
                params.get("userId"),
                params.get("username"),
                params.get("password"),
                params.get("email")

        );

        Database.addUser(user);

        response.sendRedirect("/index.html");
    }
}
