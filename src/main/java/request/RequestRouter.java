package request;

import handler.StaticFileHandler;
import handler.UserRequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class RequestRouter {

    public static void handle(String url, DataOutputStream dos) throws IOException {

        if(url.contains("?")){
            UserRequestHandler.handle(url, dos);
        }
        else StaticFileHandler.handle(url, dos);

    }

}
