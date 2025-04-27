package webserver.servlet;

import webserver.http.ContentType;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.Status;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class StaticServlet implements HttpServlet {

    private static final Set<String> STATIC_EXTENSIONS = Set.of(
            "html", "css", "js", "png", "jpg", "jpeg", "svg", "ico"
    );

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
        String trimmed = path.startsWith("/") ? path.substring(1) : path;

        if (!hasExtension(trimmed)) {
            if (!trimmed.endsWith("/")) {
                trimmed += "/";
            }
            return trimmed + "index.html";
        }

        return trimmed;
    }

    private boolean hasExtension(String path) {
        int lastDot = path.lastIndexOf('.');
        int lastSlash = path.lastIndexOf('/');
        return lastDot > lastSlash;
    }

    private ContentType getContentType(String fileName) {
        return ContentType.fromFileName(fileName);
    }
}
