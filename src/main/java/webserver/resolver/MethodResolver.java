package webserver.resolver;

import handler.CreateUserHandler;
import handler.Handler;
import handler.StaticFileHandler;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import webserver.common.HttpMethod;
import webserver.loader.FileResourceLoader;

public class MethodResolver {
    private static final Handler DEFAULT_HANDLER = new StaticFileHandler(new FileResourceLoader());
    private static final EnumMap<HttpMethod, Map<String, Handler>> ROUTES = new EnumMap<>(HttpMethod.class);

    static {
        // 초기화
        for (HttpMethod method : HttpMethod.values()) {
            ROUTES.put(method, new HashMap<>());
        }

        // enum 정의 순회하면서 등록
        for (HandlerMapping route : HandlerMapping.values()) {
            ROUTES.get(route.method).put(route.path, route.handler);
        }
    }
    public static Handler getHandlerByPath(String path, HttpMethod method) {
        return ROUTES
                .getOrDefault(method, new HashMap<>())
                .getOrDefault(path, DEFAULT_HANDLER);
    }

    private enum HandlerMapping {
        DEFAULT(HttpMethod.GET, "", DEFAULT_HANDLER),
        CREATE_USER(HttpMethod.POST, "/user/create", new CreateUserHandler());

        HttpMethod method;
        private String path;
        private Handler handler;

        HandlerMapping(HttpMethod method, String path, Handler handler) {
            this.method = method;
            this.path = path;
            this.handler = handler;
        }
    }

}
