package was;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;

import java.io.IOException;

public class IndexController {
    public void index(HttpRequest request, HttpResponse response) throws IOException {
        response.writeBody("<h1>Index world!</h1>");
    }
}
