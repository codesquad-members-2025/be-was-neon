package was.httpserver.servlet;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;

public class InternalErrorServlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.setStatus(500);
        response.writeBody("<h1>Internal Error</h1>".getBytes(UTF_8));
    }
}