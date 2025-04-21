package webserver;

import webserver.http.common.UrlPattern;
import webserver.http.request.Request;
import webserver.http.response.handler.DynamicHandler;
import webserver.http.response.handler.Handler;
import webserver.http.response.handler.StaticHandler;

public class Dispatcher {

    private Request request;

    public Dispatcher(Request request) {
        this.request = request;
    }

    public Handler dispatch() {
        String path = request.getRequestLine("path");
        if (UrlPattern.contain(path)) {
            return new DynamicHandler();
        }

        return new StaticHandler();
    }
}
