package webserver.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private static final long TIMEOUT = 30 * 60 * 1000L; // 30분
    private String sessionId;
    private Map<String, Object> attributes;
    private long lastAccessedTime;

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.attributes = new HashMap<>();
        this.lastAccessedTime = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setAttribute(String key, Object value) {
        accessed();
        attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        accessed();
        attributes.remove(key);
    }

    public void invalidate() {
        SessionManager.getInstance().invalidate(this);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - lastAccessedTime > TIMEOUT;
    }
    private void accessed(){
        lastAccessedTime = System.currentTimeMillis();
    }
}
