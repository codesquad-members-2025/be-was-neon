package was;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLog.log;

public class HttpServer {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        log("서버 시작: " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            executor.submit(new HttpRequestHandler(socket));
        }
    }
}
