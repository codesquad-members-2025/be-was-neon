package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ServletManager {
    private final Map<String, HttpServlet> servletMap = new HashMap<>();

    public void add(String pathPrefix, HttpServlet servlet) {
        servletMap.put(pathPrefix, servlet);
    }

    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        HttpServlet target = resolve(request);

        if (target != null){
            target.service(request, response);
        } else {
            response.setStatus(404);
            response.writeBody("<h1>404 Not Found</h1>".getBytes());
        }
    }

    public HttpServlet resolve(HttpRequest request) {
        String[] split = request.getPath().split("/");
        if (split.length < 2) {
            return null;
        }

        String url = URLDecoder.decode("/" + split[1], UTF_8);
        return servletMap.get(url);
    }
}
