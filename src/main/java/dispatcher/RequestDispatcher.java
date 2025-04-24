package dispatcher;

import controller.UserController;
import handler.StaticFileHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class RequestDispatcher {
    public static void dispatch(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();


        if (path.startsWith("/create") && request.getMethod().equals("POST")) {
            UserController.handleCreateUser(request, response);
        }

        else {
            StaticFileHandler.handle(request, response);
        }
    }
}
