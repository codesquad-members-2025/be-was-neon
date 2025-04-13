package was;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

import java.io.IOException;

public class IndexController {
    @Mapping("/")
    public void home(HttpRequest request, HttpResponse response) throws IOException {
        response.writeBody("<h1>Hello world!</h1>");
    }

    @Mapping("/index")
    public void index(HttpRequest request, HttpResponse response) throws IOException {
        response.writeBody("<h1>Index world!</h1>");
    }
}
