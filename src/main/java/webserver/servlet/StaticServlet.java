package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

public class StaticServlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        try {
            String resolvedPath = resolvePath(request.getPath());
            InputStream is = getClass().getClassLoader().getResourceAsStream("static/" + resolvedPath);

            if (is == null) {
                response.setStatus(404);
                response.writeBody("<h1>404 Not Found</h1>".getBytes());
                return;
            }

            byte[] content = is.readAllBytes();
            response.setStatus(200);
            response.setContentType(getContentType(resolvedPath));
            response.writeBody(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String resolvePath(String path) {
        if (path.matches(".+\\.(html|css|js|png|jpg|jpeg|svg|ico)$")) {
            return path.startsWith("/") ? path.substring(1) : path;
        }
        String trimmed = path.startsWith("/") ? path.substring(1) : path;
        return trimmed + "/index.html";
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
