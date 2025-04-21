package controller;

import db.Database;
import model.User;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class UserController {

    public static void handleCreateUser(HttpRequest request, HttpResponse response){
        User user = new User(
                request.getQueryParams().get("userId"),
                request.getQueryParams().get("password"),
                request.getQueryParams().get("name"),
                request.getQueryParams().get("email")
        );
        Database.addUser(user);

    }
}
