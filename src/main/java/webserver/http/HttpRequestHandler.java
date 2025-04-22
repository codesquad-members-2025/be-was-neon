package webserver.http;

import webserver.servlet.ServletManager;
import java.io.*;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpRequestHandler implements Runnable {
    private final Socket socket;
    private final ServletManager servletManager;

    public HttpRequestHandler(Socket socket, ServletManager servletManager) {
        this.socket = socket;
        this.servletManager = servletManager;
    }

    @Override
    public void run() {
        try{
            process();
        } catch (Exception e) {
            log(e);
        }
    }

    private void process() throws IOException {
        try(socket;
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
            OutputStream outputStream = socket.getOutputStream()
        ) {
            HttpRequest request = new HttpRequest(reader);
            HttpResponse response = new HttpResponse(outputStream);

            if (request.getPath().contains("favicon.ico")) {
                log("favicon 무시");
                return;
            }
            log("http 요청 출력:" + request);
            servletManager.execute(request, response);
            response.flush();
            log("http 응답 생성 끝!");
        }
    }
}
