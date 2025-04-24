package webserver.servlet;

import model.User;
import model.UserRepository;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpSession;

import java.io.IOException;

public class LoginServlet implements HttpServlet {
    private final HttpSession httpSession;
    private final UserRepository userRepository;

    public LoginServlet(HttpSession httpSession, UserRepository userRepository) {
        this.httpSession = httpSession;
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

        User user = userRepository.findByUserId(userId);
        if (user == null || !user.getPassword().equals(password)) {
            response.setStatus(302);
            response.addHeader("Location", "/login/login_failed.html");
            return;
        }

        String sessionId = httpSession.createSession(user);
        response.setStatus(302);
        response.addHeader("Location", "/");
        response.addHeader("Set-Cookie", "SESSIONID=" + sessionId + "; Path=/; HttpOnly");
    }
}
