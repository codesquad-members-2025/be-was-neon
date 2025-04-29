package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final String sessionId;
    private final Map<String, Object> attributes = new HashMap<>();

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }
}