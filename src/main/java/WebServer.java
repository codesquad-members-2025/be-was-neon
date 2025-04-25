import model.UserRepository;
import webserver.HttpServer;
import webserver.handler.AuthController;
import webserver.handler.Controller;
import webserver.http.HttpSession;
import webserver.servlet.*;
import java.io.IOException;
import java.util.List;

public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpSession httpSession = new HttpSession();
        UserRepository userRepository = new UserRepository();
        AuthController authController = new AuthController(httpSession, userRepository);
        List<Controller> controllers = List.of(authController);

        HttpServlet servlet = new AnnotationServlet<>(controllers);
        ServletManager servletManager = new ServletManager();
        addServletPath(servletManager);
        servletManager.setDefaultServlet(servlet);

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }

    private static void addServletPath(ServletManager servletManager) {
        servletManager.add("/", new StaticServlet());
        servletManager.add("/article", new StaticServlet());
        servletManager.add("/comment", new StaticServlet());
        servletManager.add("/main", new StaticServlet());
        servletManager.add("/registration", new StaticServlet());
        servletManager.add("/login", new StaticServlet());
    }
}
