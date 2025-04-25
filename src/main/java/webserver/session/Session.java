package webserver.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final String sessionId;
    private final long creationTime;
    private final Map<String, Object> attributes = new HashMap<>();
    private boolean isValid = true;
    private int maxInactiveInterval = 1800; //30분
    private long lastAccessedTime;

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
    }

    public void access() {
        this.lastAccessedTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        long now = System.currentTimeMillis();
        return !isValid || (now - lastAccessedTime > maxInactiveInterval*1000L);
    }
    //로그아웃: 세션 무효화 + 세션메니저에서 제거
    public void invalidate() {
        isValid = false;
        attributes.clear();
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public Object getAttributes(String key) {
        // 캡슐화 원칙 -> map반환은 하지 않는게 좋음 -> 이렇게 하면 외부에서 이 map을 직접 조작할 수 있음
        return attributes;
    }

    public boolean isValid() {
        return isValid;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

}
