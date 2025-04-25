package webserver.session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMap {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    private SessionMap() {}

    public static void putSession(String sessionId, Session session) {
        sessions.put(sessionId, session);
    }

    public static Optional<Session> getSession(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static int sessionSize() {
        return sessions.size();
    }
}
