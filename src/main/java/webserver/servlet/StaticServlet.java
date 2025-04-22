package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

public class StaticServlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        try {
            String requestPath = request.getPath();
            serveStaticFile(requestPath, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
