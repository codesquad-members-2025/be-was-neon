package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebServer {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        int port = (args == null || args.length == 0) ? DEFAULT_PORT : Integer.parseInt(args[0]);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownPool(threadPool)));
            System.out.println("Server started on port " + port);

            while (true) {
                Socket connection = listenSocket.accept();
                threadPool.execute(new RequestHandlerV2(connection));
            }
        }
    }

    private static void shutdownPool(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }
    }
}
