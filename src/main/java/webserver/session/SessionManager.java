package webserver.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    private SessionManager() {}

    private static class Holder {
        private static final SessionManager instance = new SessionManager();
    }

    public static SessionManager getInstance() {
        return Holder.instance;
    }

    public Session createSession() {
        String sid = UUID.randomUUID().toString();
        Session session = new Session(sid);
        sessions.put(sid, session);
        return session;
    }

    //사용자가 요청을 보낼 때 로그인 여부 확인/마지막 접근 시간 갱신
    public Session findSession(String sid) {
        Session session = sessions.get(sid);
        if(session==null || session.isExpired()) {
            sessions.remove(sid); //만료된 세션은 제거 (멀티스레드(동시성환경)에서는 제거가 안 될 가능성도 있음)
            return null;
        }
        session.access(); //마지막 접근 시간 갱신
        return session;
    }

    public void removeSession(String sid) {
        Session session = sessions.remove(sid);
        //remove가 실패했거나 sid가 잘못되었거나 이미 삭제했으면 session은 null
        //null이면 invalidate()는 절대 호출하면 안됨 -> NullPointException 일어남
        if (session != null) {
            session.invalidate();
        }
    }

    public Map<String, Session> getSessions() {
        return sessions;
    }
}
