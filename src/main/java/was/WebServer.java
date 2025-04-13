package was;

import was.httpserver.HttpServer;
import was.httpserver.ServletManager;
import was.servlet.CreateServlet;
import was.servlet.HomeServlet;
import was.servlet.IndexServlet;

import java.io.IOException;

public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        ServletManager servletManager = new ServletManager();
        servletManager.add("/", new HomeServlet());
        servletManager.add("/index", new IndexServlet());
        servletManager.add("/create", new CreateServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
