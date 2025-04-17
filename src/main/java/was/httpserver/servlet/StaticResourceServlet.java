package was.httpserver.servlet;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticResourceServlet implements HttpServlet {
    private final String basePath;

    public StaticResourceServlet(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getPath().replaceFirst("/static", "");
        Path filePath = Paths.get(this.basePath + requestPath);

        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
           throw new PageNotFoundException("file not found: " + filePath);
        }

        byte[] fileContent = Files.readAllBytes(filePath);
        String mimeType = URLConnection.guessContentTypeFromName(filePath.toString());
        if (mimeType == null) mimeType = "application/octet-stream";

        response.setStatus(200);
        response.setContentType(mimeType);
        response.writeBody(fileContent);
    }
}
