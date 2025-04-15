package session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {


    public static final String SESSION_COOKIE_NAME = "mySessionId";


    private static final Map<String, Object> sessionStore = new ConcurrentHashMap<>();


    public static String createSession(Object value) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);
        return sessionId;
    }


    public Object getSession(String sessionId) {
        return sessionStore.get(sessionId);
    }


    public void expireSession(String sessionId) {
        sessionStore.remove(sessionId);
    }

}
