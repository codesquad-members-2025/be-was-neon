package webserver.servlet;

import model.UserRepository;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import java.io.IOException;

public class ServletManager {
    private final UserRepository userRepository = new UserRepository();
    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        HttpServlet servlet = resolve(request, response);
        servlet.service(request, response);
    }

    public HttpServlet resolve(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        if (isStaticFile(path)) {
            HttpServlet servlet = new StaticServlet();
            return servlet;
        }

        if (path.equals("/create")&& request.getMethod().equals("POST")) {
            HttpServlet servlet = new UserCreateServlet(userRepository);
            return servlet;
        }
        return null;
    }

    private boolean isStaticFile(String path) {
        return path.matches(".+\\.(html|css|js|png|jpg|jpeg|svg|ico)$");
    }
}
