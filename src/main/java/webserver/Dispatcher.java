package webserver;
import webserver.handler.*;
import webserver.http.HttpRequest;
import webserver.util.ContentType;

//요청을 핸들러와 매핑
public class Dispatcher {
    public Handler getHandler(HttpRequest request) {
        Handler handler;
        String path = request.getPath();

        if (isStaticRequest(request)) {
            handler = new StaticFileHandler();
        } else if (path.equals("/user/create")) {
            handler = new UserCreateHandler();
        } else {
            handler = new NotFoundHandler();
        }

        return new ExceptionHandler(handler);

    }

    private boolean isStaticRequest(HttpRequest request) {
        String path =  request.getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }
        String extension = ContentType.extractExtension(path);
        return extension != null;
    }
}
