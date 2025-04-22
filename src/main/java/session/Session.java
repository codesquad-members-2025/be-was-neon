package session;

import model.User;

public class Session {
    private final String sessionId;
    private final User user;
    private final Long createdAt;

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
        this.createdAt = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public User getUser() {
        return user;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
}
