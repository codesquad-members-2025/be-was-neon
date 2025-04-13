package was;

import was.httpserver.HttpServer;
import was.httpserver.HttpServlet;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.annotation.AnnotationServlet;

import java.io.IOException;
import java.util.List;

public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new IndexController(),new CreateController());
        HttpServlet reflectionServlet = new AnnotationServlet(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(reflectionServlet);
        servletManager.add("/favicon", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
