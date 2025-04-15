package service;

import db.Database;
import dto.UserCreateRequest;
import model.User;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class UserService {


    public UserService() {
    }


    public static User createUser(UserCreateRequest request) {

        User user = new User(request.userId(), request.password(), request.name(), request.email());

        Database.addUser(user);

        return user;
    }



}
