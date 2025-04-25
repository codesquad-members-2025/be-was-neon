package util;

import model.User;
import webserver.http.HttpRequest;
import webserver.http.SessionManager;

public class SessionUtil {

    public static String getSessionIdFromCookie(HttpRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader == null) return null;

        for (String pair : cookieHeader.split(";")) {
            String[] keyValue = pair.trim().split("=");
            if (keyValue.length == 2 && keyValue[0].equals("SESSIONID")) {
                return keyValue[1];
            }
        }
        return null;
    }

    public static User getLoggedInUser(HttpRequest request, SessionManager sessionManager) {
        String sessionId = getSessionIdFromCookie(request);
        return (User) sessionManager.getSession(sessionId);
    }
}

