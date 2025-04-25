package webserver.http;

import model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static util.MyLogger.log;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private final Map<String, User> sessions = new HashMap<>();

    private SessionManager() {}

    public static SessionManager getInstance() {
        return instance;
    }

    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        log("Session이 생성되었습니다: " + sessionId);
        return sessionId;
    }

    public User getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void invalidate(String sessionId) {
        sessions.remove(sessionId);
        log("Session이 삭제되었습니다: " + sessionId);
    }
}
