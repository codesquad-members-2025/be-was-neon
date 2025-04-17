package handler;

import db.Database;
import model.User;
import utils.RequestParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class UserRequestHandler {

    public static void handle(String url, DataOutputStream dos) throws IOException {
        Map<String, String> paramMap = RequestParser.parseUserInfo(url);
        String parsedUrl = RequestParser.extractPath(url);

        User newUser = new User(
                paramMap.get("userId"),
                paramMap.get("name"),
                paramMap.get("password"),
                paramMap.get("email")
        );
        Database.addUser(newUser);

        StaticFileHandler.handle(parsedUrl, dos);
    }

}
