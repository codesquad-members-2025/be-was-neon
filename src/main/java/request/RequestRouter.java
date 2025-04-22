package request;

import handler.*;
import httpconst.HttpConst;
import response.HttpResponseWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public class RequestRouter {

    public static Handler route(Request request, DataOutputStream dos) throws IOException {

        for(HandlerManager handlerManager : HandlerManager.values()){
            if(request.getStatusLine().url().equals(handlerManager.getUrl())){
                if(request.getStatusLine().method().equals(handlerManager.getMethod())){
                    return handlerManager.getHandler();
                }
                else{
                    HttpResponseWriter.send405Error(dos, HttpConst.METHOD_POST);
                    return new ErrorHandler();
                }
            }
        }
        return new StaticFileHandler();

    }

}
