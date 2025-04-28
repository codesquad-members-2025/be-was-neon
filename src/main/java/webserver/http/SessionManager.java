package webserver.http;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Map<String, Session> sessionStore = new ConcurrentHashMap<>();

    private SessionManager() {}

    private static class Holder {
        private static final SessionManager INSTANCE = new SessionManager();
    }

    public static SessionManager getInstance() {
        return Holder.INSTANCE;
    }

    public Session createSession() {
        String sessionId = generateSessionId();
        Session session = new Session(sessionId);
        sessionStore.put(sessionId, session);
        return session;
    }

    public Optional<Session> getSession(String sessionId) {
        return Optional.ofNullable(sessionStore.get(sessionId));
    }

    public void invalidate(String sessionId) {
        sessionStore.remove(sessionId);
    }

    private String generateSessionId() {
        byte[] randomBytes = new byte[24];
        SecureRandom random = new SecureRandom();
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}