package controller;

import db.Database;
import model.User;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class UserController {


    public UserController() {
    }


    public static User createUser(String body) {

        Map<String, String> params = parseQueryString(body);
        User user = new User(params.get("userId"),params.get("password"),URLDecoder.decode(params.get("name"), StandardCharsets.UTF_8)
        ,URLDecoder.decode(params.get("email"), StandardCharsets.UTF_8));

        Database.addUser(user);

        return user;
    }

    private static Map<String, String> parseQueryString(String body) {
        return Arrays.stream(body.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr.length > 1 ? arr[1] : ""
                ));
    }

}
