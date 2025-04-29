package util;

import model.User;
import webserver.http.HttpRequest;
import webserver.http.SessionManager;
import java.util.Optional;

public class SessionUtil {

    public static Optional<String> getSessionIdFromCookie(HttpRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader == null) return Optional.empty();

        for (String pair : cookieHeader.split(";")) {
            String[] keyValue = pair.trim().split("=");
            if (keyValue.length == 2 && keyValue[0].equals("SESSIONID")) {
                return Optional.of(keyValue[1]);
            }
        }
        return Optional.empty();
    }

    public static Optional<User> getLoggedInUser(HttpRequest request, SessionManager sessionManager) {
        return getSessionIdFromCookie(request)
                .flatMap(sessionManager::getSession)
                .flatMap(session -> Optional.ofNullable((User) session.getAttribute("user")));
    }
}

