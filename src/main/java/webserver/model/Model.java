package webserver.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Model {

    private final Map<String, Object> model;

    public Model() {
        this.model = new ConcurrentHashMap<>();
    }

    public Object getAttribute(String name) {
        return model.get(name);
    }

    public void addAttribute(String name, Object value) {
        model.put(name, value);
    }

}
