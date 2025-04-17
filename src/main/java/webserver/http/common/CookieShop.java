package webserver.http.common;

import static webserver.http.common.HttpConstants.EQUALS;

public class CookieShop {

    private static final String SESSION_COOKIE_NAME = "JSESSIONID";
    private static final String COOKIE = "Cookie";
    private static final String SEMICOLON = ";";
    private static final String HTTP_ONLY = "HttpOnly";
    private static final String PATH = "Path=";
    private static final String ALLOWED_PATH = "/";


    public static String takeSessionIdFrom(HttpHeaders headers) {
        String rawCookie = headers.get(COOKIE);
        if (rawCookie == null) {
            return null;
        }

        String[] cookies = rawCookie.split(SEMICOLON);
        for (String cookie : cookies) {
            String[] keyValue = cookie.split(EQUALS);
            if (keyValue.length == 2 && keyValue[0].trim().equals(SESSION_COOKIE_NAME)) {
                return keyValue[1].trim();
            }
        }

        return null;
    }

    public static String bakeSessionCookie(String sessionId) {
        return SESSION_COOKIE_NAME + EQUALS + sessionId + SEMICOLON
                + PATH + ALLOWED_PATH + SEMICOLON
                + HTTP_ONLY;
    }

}
