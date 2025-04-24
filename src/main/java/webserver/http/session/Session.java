package webserver.http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {
    private String id;
    private Map<String, Object> attributes;

    public Session() {
        this.id = UUID.randomUUID().toString();
        this.attributes = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setAttributes(String key, Object value) {
        attributes.put(key, value);
    }
}
