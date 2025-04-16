package webserver.resolver;

import handler.CreateUserHandler;
import handler.Handler;
import handler.StaticFileHandler;
import java.io.FileNotFoundException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import webserver.common.HttpMethod;
import webserver.exception.MethodNotAllowedException;
import webserver.loader.FileResourceLoader;
import webserver.loader.ResourceLoader;

public class MethodResolver {
    private static final ResourceLoader RESOURCE_LOADER = new FileResourceLoader();
    private static final Handler DEFAULT_HANDLER = new StaticFileHandler(RESOURCE_LOADER);
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

    public static Handler getHandlerByPath(String path, HttpMethod method) throws FileNotFoundException {
        Handler handler = ROUTES.getOrDefault(method, new HashMap<>())
                .get(path);

        if (handler != null) return handler;

        // 정적 파일 경로는 존재하는 실제 파일이면 StaticFileHandler로 넘긴다
        if (method == HttpMethod.GET && RESOURCE_LOADER.exists(path)) {
            return DEFAULT_HANDLER;
        }
        throw new MethodNotAllowedException("Method " + method + " not allowed for " + path);
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
