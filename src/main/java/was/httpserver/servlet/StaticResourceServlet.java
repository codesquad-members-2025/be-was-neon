package was.httpserver.servlet;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;

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
            response.setStatus(404);
            response.writeBody("<h1>파일을 찾을 수 없습니다</h1>");
            return;
        }

        byte[] fileContent = Files.readAllBytes(filePath);
        String mimeType = URLConnection.guessContentTypeFromName(filePath.toString());
        if (mimeType == null) mimeType = "application/octet-stream";

        response.flushBinary(fileContent, mimeType, 200);
    }
}
