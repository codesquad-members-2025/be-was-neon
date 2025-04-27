package webserver;

import request.RequestHeader;
import response.handler.*;

import java.io.IOException;

public class Dispatcher {
    public enum Route{
        USER_CREATE("POST","/user/create", new CreateUserHandler()),
        USER_LOGIN("POST","/user/login", new LoginHandler()),
        USER_LOGOUT("POST","/user/logout", new LogoutHandler()),
        MAIN_PAGE("GET", "/index.html", new DynamicHeaderHandler()),
        DEFAULT_PAGE("GET", "/", new DynamicHeaderHandler()),
        USER_LIST("GET", "/user/list.html", new UserListHandler()),
        ARTICLE_FORM("GET", "/article/write.html", new AuthenticationHandler());

        private final String method;
        private final String path;
        private final Handler handler;

        Route(String method, String path, Handler handler) {
            this.method = method;
            this.path = path;
            this.handler = handler;
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
    }

    public static Handler getHandler(RequestHeader requestHeader) throws IOException {
        for (Route route : Route.values()) {
            if (requestHeader.getMethod().equals(route.getMethod()) && requestHeader.getPath().equals(route.getPath())) {
                return route.getHandler();
            }
        }
        return new StaticResourceHandler();
    }
}
