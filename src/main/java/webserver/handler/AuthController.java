package webserver.handler;

import annotation.Mapping;
import model.User;
import model.UserRepository;
import util.SessionUtil;
import webserver.http.*;
import java.io.IOException;
import java.io.InputStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import static webserver.template.Template.LOGGED_IN_HEADER;
import static webserver.template.Template.LOGGED_OUT_HEADER;

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
        response.status(Status.FOUND)
                .header("Location", "/");
    }

    @Mapping("/logina")
    public void login(HttpRequest request, HttpResponse response) throws IOException {
        if (!requirePost(request, response)) return;

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User user = userRepository.findByUserId(userId);
        if (user == null || !user.getPassword().equals(password)) {
            response.status(Status.FOUND)
                    .header("Location", "/login/login_failed.html");
            return;
        }

        String sessionId = sessionManager.createSession(user);
        response.status(Status.FOUND)
                .header("Location", "/")
                .header("Set-Cookie", "SESSIONID=" + sessionId + "; Path=/; HttpOnly");
    }

    @Mapping("/logout")
    public void logout(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = SessionUtil.getSessionIdFromCookie(request);
        //Todo: 옵셔널로 하기
        if (sessionId != null) {
            sessionManager.invalidate(sessionId);}

        response.status(Status.FOUND)
                .header("Location", "/")
                .header("Set-Cookie", "SESSIONID=; Max-Age=0; Path=/");
    }

    @Mapping("/")
    public void main(HttpRequest request, HttpResponse response) throws IOException {
        User user = SessionUtil.getLoggedInUser(request, sessionManager);

        String section = (user != null)
                ? LOGGED_IN_HEADER.replace("{{name}}", user.getName())
                : LOGGED_OUT_HEADER;

        InputStream is = getClass().getClassLoader().getResourceAsStream("static/index.html");
        String html = new String(is.readAllBytes(), UTF_8);
        String render = html.replace("{{nav}}", section);

        response.status(Status.OK)
                .contentType(ContentType.HTML)
                .body(render.getBytes(UTF_8));
    }

    private boolean requirePost(HttpRequest request, HttpResponse response) throws IOException {
        if (!"POST".equals(request.getMethod())) {
            response.status(Status.METHOD_NOT_ALLOWED)
                    .body("<h1>405 Method Not Allowed</h1>".getBytes());
            return false;
        }
        return true;
    }
}
