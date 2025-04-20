package webserver;

import common.UrlPattern;
import request.Request;
import response.handler.DynamicHandler;
import response.handler.Handler;
import response.handler.StaticHandler;

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
