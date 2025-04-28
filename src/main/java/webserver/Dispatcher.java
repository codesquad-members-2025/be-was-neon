package webserver;

import request.RequestHeader;
import response.handler.*;

import java.io.IOException;

public class Dispatcher {
    public enum Route{
        USER_CREATE("POST","/user/create", new CreateUserHandler(), RouteType.EXACT),
        USER_LOGIN("POST","/user/login", new LoginHandler(), RouteType.EXACT),
        USER_LOGOUT("POST","/user/logout", new LogoutHandler(), RouteType.EXACT),
        MAIN_PAGE("GET", "/index.html", new DynamicHeaderHandler(), RouteType.EXACT),
        DEFAULT_PAGE("GET", "/", new DynamicHeaderHandler(), RouteType.EXACT),
        USER_LIST("GET", "/user/list.html", new UserListHandler(), RouteType.EXACT),
        ARTICLE_FORM("GET", "/article/write.html", new AuthenticationHandler(), RouteType.EXACT),
        ARTICLE_CREATE("POST", "/article", new CreateArticleHandler(), RouteType.EXACT),
        ARTICLE_SHOW("GET","/articles",new ArticleShowHandler(),RouteType.PREFIX);

        public enum RouteType{
            EXACT, PREFIX;
        }

        private final String method;
        private final String path;
        private final Handler handler;
        private final RouteType routeType;

        Route(String method, String path, Handler handler, RouteType routeType) {
            this.method = method;
            this.path = path;
            this.handler = handler;
            this.routeType = routeType;
        }

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }

        public Handler getHandler() {
            return handler;
        }

        public RouteType getRouteType() {
            return routeType;
        }
    }

    public static Handler getHandler(RequestHeader requestHeader) throws IOException {
        for (Route route : Route.values()) {
            if(route.getRouteType() == Route.RouteType.EXACT) {
                if (requestHeader.getMethod().equals(route.getMethod()) && requestHeader.getPath().equals(route.getPath())) {
                    return route.getHandler();
                }
            } else {
                if (requestHeader.getMethod().equals(route.getMethod()) && requestHeader.getPath().startsWith(route.getPath())) {
                    return route.getHandler();
                }
            }
        }
        return new StaticResourceHandler();
    }
}
