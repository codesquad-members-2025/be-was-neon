package was;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

public class CreateController {
    @Mapping("/create")
    public void create(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        response.writeBody("<h1>User Info</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>ID: " + userId + "</li>");
        response.writeBody("<li>Name: " + name + "</li>");
        response.writeBody("<li>Email: " + email + "</li>");
        response.writeBody("</ul>");
    }
}
