package webserver.servlet;

import webserver.http.ContentType;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.Status;
import java.io.IOException;
import java.io.InputStream;

public class StaticServlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        try {
            String resolvedPath = resolvePath(request.getPath());
            InputStream is = getClass().getClassLoader().getResourceAsStream("static/" + resolvedPath);

            if (is == null) {
                response.status(Status.NOT_FOUND)
                        .body("<h1>404 Not Found</h1>".getBytes());
                return;
            }

            byte[] content = is.readAllBytes();
            response.status(Status.OK)
                    .contentType(getContentType(resolvedPath))
                    .body(content);

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

    private ContentType getContentType(String fileName) {
        return ContentType.fromFileName(fileName);
    }
}
