package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServletManager {
    private final Map<String, HttpServlet> servletMap = new HashMap<>();
    public void add(String path, HttpServlet servlet) {
        servletMap.put(path, servlet);
    }

    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        HttpServlet servlet = resolve(request, response);
        if (servlet == null) {
            response.setStatus(404);
            response.writeBody("<h1>404 Not Found</h1>".getBytes());
            return;
        }
        servlet.service(request, response);
    }

    public HttpServlet resolve(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        if (isStaticFile(path)) {
            HttpServlet servlet = new StaticServlet();
            return servlet;
        }

        if (servletMap.containsKey(path)) {
            return servletMap.get(path);
        }

        return new StaticServlet();
    }

    private boolean isStaticFile(String path) {
        return path.matches(".+\\.(html|css|js|png|jpg|jpeg|svg|ico)$");
    }
}
