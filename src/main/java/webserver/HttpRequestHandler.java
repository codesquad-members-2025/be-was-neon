package webserver;

import java.io.*;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpRequestHandler implements Runnable {
    private final Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
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
            getRequest(request, response);
            response.flush();
            log("http 응답 생성 끝!");
        }
    }

    private void getRequest(HttpRequest request, HttpResponse response) throws IOException {
        try {
            String path = request.getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }

            String resourcePath = "static/main" + path;
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);

            if (is == null) {
                response.setStatus(404);
                response.writeBody("<h1>404 Not Found</h1>".getBytes(UTF_8));
                return;
            }

            byte[] body = is.readAllBytes();
            response.setStatus(200);
            response.writeBody(body);
        } catch (IOException e) {
            log(e);
        }
    }
}
