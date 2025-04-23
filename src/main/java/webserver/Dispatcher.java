package webserver;

import request.RequestHeader;
import response.handler.*;

import java.io.IOException;

public class Dispatcher {
    public enum Route{
        USER_CREATE("POST","/user/create", new CreateUserHandler()),
        USER_LOGIN("POST","/user/login", new LoginHandler()),
        USER_LOGOUT("POST","/user/logout", new LogoutHandler());

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
            if (requestHeader.getMethod().equals(route.getMethod()) && requestHeader.getPath().startsWith(route.getPath())) {
                return route.getHandler();
            }
        }
        return new StaticResourceHandler();
    }
}
