package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.util.RequestParser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // in(inputstream)을 감싸서 텍스트 데이터를 줄 단위로 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //out(outputstream)을 감싸서 데이터를 바이트 단위로 출력
            DataOutputStream dos = new DataOutputStream(out);
            // ✅ 요청 경로 파싱
            String path = RequestParser.parseRequestPath(br);
            logger.debug("요청 경로: {}", path);

            InputStream resource = getClass().getClassLoader().getResourceAsStream("static" + path);
            if (resource == null) {
                send404(dos);
                return;
            }

            byte[] body = resource.readAllBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void send404(OutputStream out) throws IOException {
        String body = "<h1>404 Not Found</h1>";
        byte[] bytes = body.getBytes();

        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + bytes.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(bytes, 0, bytes.length);
        dos.flush();
    }


}
