package webserver.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    private SessionManager() {}

    public static SessionManager getInstance() {
        return instance;
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
        //todo 이 로직 필요한가? 만료되었는데 제거 안되어있는 경우가 있나?
        if(session==null || session.isExpired()) {
            sessions.remove(sid); //만료된 세션은 제거 (멀티스레드(동시성환경)에서는 제거가 안 될 가능성도 있음)
            return null;
        }
        session.access(); //마지막 접근 시간 갱신
        return session;
    }

    public void removeSession(String sid) {
        Session session = sessions.remove(sid);
        //todo 여기서 왜 session!=null을 검사해줘야 하지? null인 경우가 있나?
        // 그냥 바로 invalidate()하면 되지 않나?
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
