package handler;

import httpconst.HttpConst;

public enum HandlerManager {

    USER_CREATE_POST(HttpConst.METHOD_POST, "/user/create", new UserRequestHandler()),
    USER_LOGIN_POST(HttpConst.METHOD_POST, "/login", new UserLoginHandler()),
    USER_LOGOUT_POST(HttpConst.METHOD_POST, "/user/logout", new UserLogoutHandler());

    private final String method;
    private final String url;
    private final Handler handler;

    HandlerManager(String method, String url, Handler handler) {
        this.method = method;
        this.url = url;
        this.handler = handler;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Handler getHandler() {
        return handler;
    }

}
