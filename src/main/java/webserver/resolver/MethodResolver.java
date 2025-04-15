package webserver.resolver;

import handler.CreateUserHandler;
import handler.Handler;
import handler.StaticFileHandler;
import webserver.common.HttpMethod;
import webserver.loader.FileResourceLoader;

public enum MethodResolver {
    DEFAULT("", new StaticFileHandler(new FileResourceLoader())),
    CREATE_USER("/user/create", new CreateUserHandler());

    private String path;
    private Handler handler;

    MethodResolver(String path, Handler handler) {
        this.path = path;
        this.handler = handler;
    }

    public static Handler getHandlerByPath(String path, HttpMethod method) {
        if (method.equals(HttpMethod.GET)) return DEFAULT.handler;

        for (MethodResolver value : MethodResolver.values()) {
            if (value.path.equals(path)) return value.handler;
        }
        return DEFAULT.handler;
    }
}
