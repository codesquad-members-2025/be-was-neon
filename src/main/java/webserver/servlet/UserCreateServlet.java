package webserver.servlet;

import model.User;
import model.UserRepository;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class UserCreateServlet implements HttpServlet {
    private final UserRepository userRepository;

    public UserCreateServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        userRepository.save(new User(userId, password, name, email));

        // 여기서 redirect
        response.setStatus(302);
        response.addHeader("Location", "/main/index.html");
    }
}
