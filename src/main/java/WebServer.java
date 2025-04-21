import webserver.HttpServer;

import java.io.IOException;

public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(PORT);
        server.start();
    }
}
