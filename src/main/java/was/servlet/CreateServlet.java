package was.servlet;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;

import java.io.IOException;

public class CreateServlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
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
