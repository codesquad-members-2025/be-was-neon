package session;

import model.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// 객체 자체를 싱글턴 패턴으로 해야함
public class SessionManager {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    private SessionManager() {}

    private static class Holder {
        private static final SessionManager INSTANCE = new SessionManager();
    }

    public static SessionManager getInstance() {
        return Holder.INSTANCE;
    }

    public Session createSession(User user){
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, user);
        sessions.put(sessionId, session);
        return session;
    }

    public Optional<Session> getSession(String sessionId){
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public void removeSession(String sessionId){
        Session session = sessions.get(sessionId);
        if(session != null){
            sessions.remove(sessionId);
        }
    }



}
