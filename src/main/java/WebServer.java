import webserver.HttpServer;
import webserver.servlet.ServletManager;

import java.io.IOException;

public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        ServletManager servletManager = new ServletManager();
        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
