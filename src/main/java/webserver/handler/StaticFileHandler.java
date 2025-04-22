package webserver.handler;

import webserver.exception.NotFoundException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.InputStream;
import java.io.IOException;

public class StaticFileHandler implements Handler {
    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }

        try (InputStream resource = getClass().getClassLoader().getResourceAsStream("static" + path)) {
            if (resource == null) {
                throw new NotFoundException();
            }
            byte[] body = resource.readAllBytes();
            response.send200Response(body, path);
        }
    }
}
