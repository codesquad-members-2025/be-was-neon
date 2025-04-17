package was.httpserver;

import was.httpserver.servlet.InternalErrorServlet;
import was.httpserver.servlet.NotFoundServlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ServletManager {
    private final Map<String, HttpServlet> servletMap = new HashMap<>();
    private HttpServlet defaultServlet;
    private HttpServlet notFoundErrorServlet =  new NotFoundServlet();
    private HttpServlet internalErrorServlet =  new InternalErrorServlet();

    public void add(String path, HttpServlet servlet) {
        servletMap.put(path, servlet);
    }

    public void setDefaultServlet(HttpServlet defaultServlet) {
        this.defaultServlet = defaultServlet;
    }
    public void setNotFoundErrorServlet(HttpServlet notFoundErrorServlet) {
        this.notFoundErrorServlet = notFoundErrorServlet;
    }
    public void setInternalErrorServlet(HttpServlet internalErrorServlet) {
        this.internalErrorServlet = internalErrorServlet;
    }

    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        try {
            HttpServlet servlet = resolveServlet(request, response);
            if (servlet == null) return;

            servlet.service(request, response);

        } catch (PageNotFoundException e) {
            e.printStackTrace();
            notFoundErrorServlet.service(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            internalErrorServlet.service(request, response);
        }
    }

    private HttpServlet resolveServlet(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getPath();
        if (isStaticFile(requestPath)) {
            serveStaticFile(requestPath, response);
            return null;
        }

        HttpServlet servlet = servletMap.get(requestPath);
        if (servlet == null) {
            for (Map.Entry<String, HttpServlet> entry : servletMap.entrySet()) {
                String prefix = entry.getKey();
                if (requestPath.startsWith(prefix)) {
                    servlet = entry.getValue();
                    break;
                }
            }
        }

        if (servlet == null) {
            servlet = defaultServlet;
        }

        if (servlet == null) {
            throw new PageNotFoundException("request url= " + requestPath);
        }
        return servlet;
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
