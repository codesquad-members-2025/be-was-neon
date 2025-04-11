package was;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLog.log;

public class HttpRequestHandler implements Runnable {
    private final Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
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
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), false, UTF_8)) {

            HttpRequest request = new HttpRequest(reader);
            HttpResponse response = new HttpResponse(writer);

            if (request.getPath().equals("/favicon.ico")) {
                log("favicon 요청");
                return;
            }

            log("http 요청 출력");
            System.out.println(request);

            log("http 응답 생성중");

            // '/' 뒤에 한 칸 띄워줘야 함
            if (request.getPath().equals("/")) {
                home(response);
            } else if (request.getPath().equals("/index")) {
                index(response);
            } else if (request.getPath().equals("/create")) {
                create(request, response);
            } else {
                notFound(response);
            }
            response.flush();
            log("http 응답 생성 끝!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void home(HttpResponse response) {
        response.writeBody("<h1>Hello world!</h1>");
    }

    private void index(HttpResponse response) {
        response.writeBody("<h1>Index world!</h1>");
    }

    private void create(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        response.writeBody("<h1>User Info</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>ID: " + userId + "</li>");
        response.writeBody("<li>Name: " + name + "</li>");
        response.writeBody("<li>Email: " + email + "</li>");
        response.writeBody("</ul>");
    }

    private static void notFound(HttpResponse response) {
        response.setStatus(404);
        response.writeBody("<h1>404 페이지를 찾을 수 없습니다.</h1>");
    }

    private void Sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
