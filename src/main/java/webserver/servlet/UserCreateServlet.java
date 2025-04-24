package webserver.servlet;

import model.User;
import model.UserRepository;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import java.io.IOException;

public class UserCreateServlet implements HttpServlet {
    private final UserRepository userRepository;

    public UserCreateServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if (!"POST".equals(request.getMethod())) {
            response.setStatus(405);
            response.writeBody("<h1>405 Method Not Allowed</h1>".getBytes());
            return;
        }

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        userRepository.save(new User(userId, password, name, email));

        // 여기서 redirect
        response.setStatus(302);
        response.addHeader("Location", "/main");
    }
}