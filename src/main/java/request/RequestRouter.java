package request;

import handler.ErrorHandler;
import handler.Handler;
import handler.HandlerManager;
import handler.StaticFileHandler;

import java.io.IOException;

public class RequestRouter {

    public static Handler route(Request request) throws IOException {

        for(HandlerManager handlerManager : HandlerManager.values()){
            if(request.getStatusLine().url().equals(handlerManager.getUrl())){
                if(request.getStatusLine().method().equals(handlerManager.getMethod())){
                    return handlerManager.getHandler();
                }
                else{
                    return new ErrorHandler();
                }
            }
        }
        return new StaticFileHandler();
    }
}
