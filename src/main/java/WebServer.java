import model.UserRepository;
import webserver.HttpServer;
import webserver.http.HttpSession;
import webserver.servlet.*;

import java.io.IOException;

public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpSession httpSession = new HttpSession();
        UserRepository userRepository = new UserRepository();
        ServletManager servletManager = new ServletManager();
        addServletPath(servletManager, userRepository, httpSession);
        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }

    private static void addServletPath(ServletManager servletManager, UserRepository userRepository, HttpSession httpSession) {
        servletManager.add("/", new StaticServlet());
        servletManager.add("/article", new StaticServlet());
        servletManager.add("/comment", new StaticServlet());
        servletManager.add("/main", new StaticServlet());
        servletManager.add("/registration", new StaticServlet());
        servletManager.add("/create", new UserCreateServlet(userRepository));
        servletManager.add("/logina", new LoginServlet(httpSession, userRepository));
        servletManager.add("/logout", new LogoutServlet(httpSession));
    }
}
