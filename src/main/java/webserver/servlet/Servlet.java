package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class Servlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {

    }

    private void getRequest(HttpRequest request, HttpResponse response) throws IOException {
        try {
            String path = request.getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }

            String resourcePath = "static/main" + path;
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);

            if (is == null) {
                response.setStatus(404);
                response.writeBody("<h1>404 Not Found</h1>".getBytes(UTF_8));
                return;
            }

            byte[] body = is.readAllBytes();
            response.setStatus(200);
            response.writeBody(body);
        } catch (IOException e) {
            log(e);
        }
    }
}
