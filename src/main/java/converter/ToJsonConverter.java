package converter;

import model.User;

public class ToJsonConverter {

    public static String convertUserToJson(User user) {
        return String.format(
                "{\"userId\":\"%s\",\"name\":\"%s\",\"email\":\"%s\"}",
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }


}
