package webserver;

import request.RequestHeader;
import response.handler.CreateUserHandler;
import response.handler.Handler;
import response.handler.StaticResourceHandler;

import java.io.IOException;

public class Dispatcher {
    public enum Route{
        USER_CREATE("post","/user/create", new CreateUserHandler());

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
            if (requestHeader.getPath().equals(route.getMethod()) && requestHeader.getPath().startsWith(route.getPath())) {
                return route.getHandler();
            }
        }
        return new StaticResourceHandler();
    }
}
