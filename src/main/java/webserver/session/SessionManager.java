package webserver.session;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private Map<String, Session> sessionMap;

    public SessionManager() {
        sessionMap = new ConcurrentHashMap<>();
        removeExpiredSession();
    }
    private static class Holder{
        private static final SessionManager INSTANCE = new SessionManager();

    }
    public static SessionManager getInstance() {
        return Holder.INSTANCE;
    }

    public Session getSession(String sessionId) {
        Session session = sessionMap.get(sessionId);
        if (session != null && !session.isExpired()) {
            return session;
        }

        return createSession();
    }

    private Session createSession() {
        String newSessionId = generateSessionId();
        Session newSession = new Session(newSessionId);
        sessionMap.put(newSessionId, newSession);
        return newSession;
    }

    public void invalidate(Session session) {
        sessionMap.remove(session.getSessionId());
    }
    public void removeExpiredSession() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            sessionMap.entrySet().removeIf(entry -> {
                Session session = entry.getValue();
                return session != null && session.isExpired();
            });
        }, 5, 5, TimeUnit.MINUTES);
    }
    private String generateSessionId() {
        byte[] randomBytes = new byte[24]; // 24 bytes = 192 bits
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
