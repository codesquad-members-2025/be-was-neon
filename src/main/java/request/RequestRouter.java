package request;

import handler.StaticFileHandler;
import handler.UserRequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class RequestRouter {

    public static void handle(RequestStatusLine requestStatusLine, DataOutputStream dos) throws IOException {

        // 분기를 나눌 때 이런 방식도 맞을지 생각해볼것
        if(requestStatusLine.url().contains("?")){
            UserRequestHandler.handle(requestStatusLine, dos);
        }
        else StaticFileHandler.handle(requestStatusLine, dos);


    }

}
