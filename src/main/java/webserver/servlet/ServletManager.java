package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.Status;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServletManager {
    private final Map<String, HttpServlet> servletMap = new HashMap<>();
    private HttpServlet defaultServlet;

    public void add(String path, HttpServlet servlet) {
        servletMap.put(path, servlet);
    }

    public void setDefaultServlet(HttpServlet servlet) {
        defaultServlet = servlet;
    }

    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        HttpServlet servlet = resolve(request, response);
        if (servlet == null) {
            response.status(Status.NOT_FOUND)
                    .body("<h1>404 Not Found</h1>".getBytes());
            return;
        }
        servlet.service(request, response);
    }

    public HttpServlet resolve(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        if (isStaticFile(path) || servletMap.containsKey(path)) {
            return new StaticServlet();
        }

        return defaultServlet;
    }

    private boolean isStaticFile(String path) {
        return path.matches(".+\\.(html|css|js|png|jpg|jpeg|svg|ico)$");
    }
}
