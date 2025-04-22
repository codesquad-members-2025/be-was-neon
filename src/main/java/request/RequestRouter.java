package request;

import handler.StaticFileHandler;
import handler.UserRequestHandler;
import httpconst.HttpConst;
import response.HttpResponseWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public class RequestRouter {

    public static void handle(Request request, DataOutputStream dos) throws IOException {

        // 분기를 나눌 때 이런 방식도 맞을지 생각해볼것
        if(request.getStatusLine().url().startsWith("/user/create")){
            if(request.getStatusLine().method().equals(HttpConst.METHOD_POST)){
                UserRequestHandler.handle(request, dos);
            }
            else HttpResponseWriter.send405Error(dos, HttpConst.METHOD_POST);
        }
        else StaticFileHandler.handle(request, dos);

    }

}
