package session;

import model.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static Session createSession(User user) {
        String sessionId;
        do {
            sessionId = UUID.randomUUID().toString();
        } while (sessions.containsKey(sessionId));

        Session session = new Session(sessionId, user);
        sessions.put(sessionId, session);
        return session;
    }

    public static Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
