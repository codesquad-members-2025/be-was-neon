package utils.parser;

import model.User;

public class ToJsonParser {

    public static String convertUserToJson(User user) {
        return String.format(
                "{\"userId\":\"%s\",\"name\":\"%s\",\"email\":\"%s\"}",
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }


}
