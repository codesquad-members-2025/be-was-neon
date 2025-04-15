package service;

import db.Database;
import dto.UserCreateRequest;
import model.User;

public class UserService {


    public UserService() {
    }


    public static User createUser(UserCreateRequest request) {

        User user = new User(request.userId(), request.password(), request.name(), request.email());

        Database.addUser(user);

        return user;
    }



}
