package was;

import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

public class IndexController {
    @Mapping("/")
    public void home(HttpResponse response) {
        response.writeBody("<h1>Hello world!</h1>");
    }

    @Mapping("/index")
    public void index(HttpResponse response) {
        response.writeBody("<h1>Index world!</h1>");
    }
}
