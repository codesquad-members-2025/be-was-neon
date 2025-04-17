package was.httpserver.servlet;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;

public class NotFoundServlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.setStatus(404);
        response.writeBody("<h1>404 페이지를 찾을 수 없습니다.</h1>".getBytes(UTF_8));
    }
}
