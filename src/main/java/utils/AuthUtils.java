package utils;

import dto.HttpRequest;
import model.User;
import session.SessionManager;

import java.util.Map;

public class AuthUtils {
    public static boolean isAuthenticated(HttpRequest request) {
        Map<String, String> cookies = request.cookies();
        return cookies.containsKey("SESSION_ID") && SessionManager.isValidSession(cookies.get("SESSION_ID"));
    }

    public static User getCurrentUserId(HttpRequest request) {
        String sessionId = request.cookies().get("SESSION_ID");
        return (User) SessionManager.getSession(sessionId); // userId 반환
    }
}