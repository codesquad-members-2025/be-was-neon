package session;

import model.User;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static Session createSession(User user){
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, user);
        sessions.put(sessionId, session);
        return session;
    }

    public static Session getSession(String sessionId){
        Session session = sessions.get(sessionId);
        if(session == null){
            throw new NoSuchElementException("해당 세션이 존재하지 않습니다.");
        }
        return session;
    }

    public static void removeSession(String sessionId){
        Session session = sessions.get(sessionId);
        if(session != null){
            sessions.remove(sessionId);
        }
    }



}
