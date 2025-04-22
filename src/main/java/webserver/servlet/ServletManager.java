package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ServletManager {
    private final Map<String, HttpServlet> servletMap = new HashMap<>();

    public void add(String pathPrefix, HttpServlet servlet) {
        servletMap.put(pathPrefix, servlet);
    }

    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        try {
            resolve(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resolve(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getPath();

        if (isStaticFile(requestPath)) {
            serveStaticFile(requestPath, response);
            return;
        }
    }

    private boolean isStaticFile(String path) {
        return path.matches(".+\\.(html|css|js|png|jpg|jpeg|svg|ico)$");
    }

    private void serveStaticFile(String path, HttpResponse response) throws IOException {
        String cssPath = path.startsWith("/") ? path.substring(1) : path;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/" + cssPath);
        byte[] content = inputStream.readAllBytes();
        response.setStatus(200);
        response.setContentType(getContentType(cssPath));
        response.writeBody(content);
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".html")) return "text/html; charset=UTF-8";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "application/javascript";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".svg")) return "image/svg+xml";
        if (fileName.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }
}
