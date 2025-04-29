package webserver;
import webserver.handler.*;
import webserver.http.HttpRequest;
import webserver.util.ContentType;
import webserver.util.Route;

import java.util.Map;

//요청을 핸들러와 매핑
public class Dispatcher {

    private final Map<Route, Handler> routeMap = Map.of(
            new Route("POST", "/user/create"), new UserCreateHandler(),
            new Route("POST", "/user/login"), new UserLoginHandler(),
            new Route("GET", "/user/logout"), new AuthHandler(new UserLogoutHandler())
    );

    public Handler getHandler(HttpRequest request) {
        if (isStaticRequest(request)) {
            return new ExceptionHandler(new StaticFileHandler());
        }

        Route route = new Route(request.getMethod(), request.getPath());
        Handler handler = routeMap.get(route);

        if (handler == null) {
            handler = new NotFoundHandler();
        }

        return new ExceptionHandler(handler);

    }

    private String convertRootPath(String path) {
        if (path.equals("/")) {
            return "/index.html";
        }
        return path;
    }

    private boolean isStaticRequest(HttpRequest request) {
        String path =  convertRootPath(request.getPath());
        String extension = ContentType.extractExtension(path);
        return extension != null;
    }
}
