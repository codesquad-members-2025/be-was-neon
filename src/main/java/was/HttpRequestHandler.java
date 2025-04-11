package was;

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

            String requestString = requestToString(reader);

            if (requestString.contains("/favicon.ico")) {
                log("favicon 요청");
                return;
            }

            log("http 요청 출력");
            System.out.println(requestString);

            log("http 응답 생성중");

            // '/' 뒤에 한 칸 띄워줘야 함
            if (requestString.startsWith("GET / ")) {
                home(writer);
            } else if (requestString.startsWith("GET /index")) {
                index(writer);
            } else if (requestString.startsWith("GET /create")) {
                create(writer);
            } else {
                notFound(writer);
            }
            log("http 응답 생성 끝!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String requestToString(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private void home(PrintWriter writer) {
        String body = "<h1>Hello world!</h1>";
        int length = body.getBytes(UTF_8).length;

        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println("Content-Length: " + length);
        writer.println();
        writer.println(body);
        writer.flush();
    }

    private void index(PrintWriter writer) {
        String body = "<h1>Index world!</h1>";
        int length = body.getBytes(UTF_8).length;

        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println("Content-Length: " + length);
        writer.println();
        writer.println(body);
        writer.flush();
    }

    private void create(PrintWriter writer) {
        // 나중에 만들게용
    }

    private static void notFound(PrintWriter writer) {
        String body = "<h1>404 페이지를 찾을 수 없습니다.</h1>";
        int length = body.getBytes(UTF_8).length;

        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println("Content-Length: " + length);
        writer.println();
        writer.println(body);
        writer.flush();
    }

    private void Sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
