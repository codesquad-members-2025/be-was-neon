package webserver.session;

import model.User;

public class Session {
    private final String sessionId;
    private final User user;
    private final long createdAt;

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
        this.createdAt = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;ㅓㅓ
    }

    public User getUser() {
        return user;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isExpired(long timeoutMillis) {
        return System.currentTimeMillis() - createdAt > timeoutMillis;
    }
}
