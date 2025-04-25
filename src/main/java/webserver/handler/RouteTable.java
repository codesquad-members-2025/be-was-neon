package webserver.handler;

import java.util.HashMap;
import java.util.Map;

public enum RouteTable {
    GET(new HashMap<>()),
    POST(new HashMap<>()),
    PUT(new HashMap<>()),
    PATCH(new HashMap<>()),
    DELETE(new HashMap<>()),
    HEAD(new HashMap<>()),
    OPTIONS(new HashMap<>()),
    TRACE(new HashMap<>()),
    CONNECT(new HashMap<>());

    private final Map<String, Handler> pathToHandler;

    RouteTable(Map<String, Handler> pathToHandler) {
        this.pathToHandler = pathToHandler;
    }

    public void registerHandler(String path, Handler handler) {
        pathToHandler.put(path, handler);
    }

    public Handler getHandler(String path) {
        return pathToHandler.get(path);
    }

    public static RouteTable from(String method) {
        return RouteTable.valueOf(method.toUpperCase());
    }
}
