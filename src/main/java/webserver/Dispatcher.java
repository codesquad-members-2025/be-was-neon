package webserver;
import webserver.handler.*;
import webserver.http.HttpRequest;
import webserver.util.ContentType;

//요청을 핸들러와 매핑
public class Dispatcher {
    public Handler getHandler(HttpRequest request) {
        if (isStaticRequest(request)) {
            return new StaticFileHandler();
        }
        String path = request.getPath();

        if (path.equals("/user/create")) {
            return new UserCreateHandler();
        }

        return new NotFoundHandler();

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
