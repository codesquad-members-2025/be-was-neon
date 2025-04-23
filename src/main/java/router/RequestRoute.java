package router;

import router.handler.HttpRequestHandler;
import router.handler.impl.UserCreationHandler;
import java.util.*;

public enum RequestRoute {

    CREATE_USER("POST", "/create", new UserCreationHandler());

    private final String method;
    private final String path;
    private final HttpRequestHandler handler;

    public static final Map<String, Set<String>> ALLOWED_METHODS = new HashMap<>();

    static {
        for (RequestRoute route : values()) {
            ALLOWED_METHODS.computeIfAbsent(route.getPath(), k -> new HashSet<>()).add(route.getMethod());
        }
    }

    RequestRoute(String method, String path, HttpRequestHandler handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public HttpRequestHandler getHandler() {
        return handler;
    }

    public static RequestRoute findByMethodAndPath(String method, String path) {
        for (RequestRoute route : values()) {
            if (route.getPath().equals(path) && route.getMethod().equals(method)) {
                return route;
            }
        }
        return null;
    }

    public static boolean isMethodAllowed(String method, String path) {
        Set<String> allowedMethods = ALLOWED_METHODS.get(path);
        return allowedMethods != null && allowedMethods.contains(method);
    }

    public static Set<String> getAllowedMethods(String path) {
        return ALLOWED_METHODS.getOrDefault(path, Set.of());
    }
}
