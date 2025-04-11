package webserver;

import request.RequestHeader;
import response.handler.CreateUserHandler;
import response.handler.Handler;
import response.handler.StaticResourceHandler;

import java.io.IOException;

public class Dispatcher {
    public static Handler getHandler(RequestHeader requestHeader) throws IOException {
        Handler handler;
        if(requestHeader.getPath().startsWith("/user/create")){
            handler = new CreateUserHandler();
        } else{
            handler = new StaticResourceHandler();
        }
        return handler;
    }
}
