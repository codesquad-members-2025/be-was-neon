package was.httpserver;

import java.io.*;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLog.log;

public class HttpRequestHandler implements Runnable {
    private final Socket socket;
    private final ServletManager servletManager;

    public HttpRequestHandler(Socket socket, ServletManager servletManager) {
        this.socket = socket;
        this.servletManager = servletManager;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            log(e);
        }
    }

    private void process() {
        try(socket;
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), false, UTF_8);
            OutputStream outputStream = socket.getOutputStream();) {

            HttpRequest request = new HttpRequest(reader);
            HttpResponse response = new HttpResponse(writer, outputStream);

            log("http 요청 출력" + request);
            servletManager.execute(request, response);
            response.flush();
            log("http 응답 생성 끝!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
