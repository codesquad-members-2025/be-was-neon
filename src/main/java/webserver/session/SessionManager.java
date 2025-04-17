package webserver.session;

import webserver.http.common.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final SessionManager instance = new SessionManager();
    private static final SecureRandom random = new SecureRandom();
    private static final int SESSION_ID_LENGTH = 16;
    private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public HttpSession getOrCreateSession(String sessionId) {
        if (sessionId == null) {
            sessionId = generateSessionId();
        }

        return sessions.computeIfAbsent(sessionId, HttpSession::new);
    }

    private String generateSessionId() {
        byte[] bytes = new byte[SESSION_ID_LENGTH];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

}
