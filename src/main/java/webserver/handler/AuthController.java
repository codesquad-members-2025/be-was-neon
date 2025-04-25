package webserver.handler;

import annotation.Mapping;
import model.User;
import model.UserRepository;
import util.SessionUtil;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.SessionManager;

import java.io.IOException;

public class AuthController implements Controller {
    private final SessionManager sessionManager;
    private final UserRepository userRepository;

    public AuthController(SessionManager sessionManager, UserRepository userRepository) {
        this.sessionManager = sessionManager;
        this.userRepository = userRepository;
    }

    @Mapping("/create")
    public void create(HttpRequest request, HttpResponse response) throws IOException {
        if (!requirePost(request, response)) return;

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        userRepository.save(new User(userId, password, name, email));

        // 여기서 redirect
        response.setStatus(302);
        response.addHeader("Location", "/main");
    }

    @Mapping("/logina")
    public void login(HttpRequest request, HttpResponse response) throws IOException {
        if (!requirePost(request, response)) return;

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User user = userRepository.findByUserId(userId);
        if (user == null || !user.getPassword().equals(password)) {
            response.setStatus(302);
            response.addHeader("Location", "/login/login_failed.html");
            return;
        }

        String sessionId = sessionManager.createSession(user);
        response.setStatus(302);
        response.addHeader("Location", "/");
        response.addHeader("Set-Cookie", "SESSIONID=" + sessionId + "; Path=/; HttpOnly");
    }

    @Mapping("/logout")
    public void logout(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = SessionUtil.getSessionIdFromCookie(request);
        if (sessionId != null) {
            sessionManager.invalidate(sessionId);}

        response.setStatus(302);
        response.addHeader("Location", "/main");
        response.addHeader("Set-Cookie", "SESSIONID=; Max-Age=0; Path=/");
    }

    private boolean requirePost(HttpRequest request, HttpResponse response) throws IOException {
        if (!"POST".equals(request.getMethod())) {
            response.setStatus(405);
            response.writeBody("<h1>405 Method Not Allowed</h1>".getBytes());
            return false;
        }
        return true;
    }
}
