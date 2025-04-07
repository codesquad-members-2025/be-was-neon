package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            Map<String, String> headers = parseRequestHeader(br);
            String requestLine = headers.get("Request-Line");
            if (requestLine == null || requestLine.isEmpty()) {
                send400Response(dos);
                return;
            }

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2) {
                send400Response(dos);
                return;
            }

            String uri = tokens[1];
            InputStream fileIn = getClass().getClassLoader().getResourceAsStream("static" + uri);
            if (fileIn != null) {
                // 파일을 읽어 응답 본문에 담음
                byte[] body = fileIn.readAllBytes();
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {
                logger.error("File not found: static{}", uri);
                String notFoundHtml = "<h1>404 Not Found</h1>";
                byte[] notFoundBody = notFoundHtml.getBytes();
                response404Header(dos, notFoundBody.length);
                responseBody(dos, notFoundBody);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String, String> parseRequestHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();

        // 요청 라인(예: "GET / HTTP/1.1")을 읽고 저장
        String requestLine = br.readLine();
        headers.put("Request-Line", requestLine);
        logger.debug(requestLine);

        String line;
        // 요청 헤더를 읽고 저장
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            logger.debug(line);
            int colonIndex = line.indexOf(":");
            if (colonIndex != -1) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }

        return headers;
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

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void send400Response(DataOutputStream dos) throws IOException {
        String badRequestHtml = "<h1>400 Bad Request</h1>";
        byte[] badRequestBody = badRequestHtml.getBytes();
        dos.writeBytes("HTTP/1.1 400 Bad Request\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + badRequestBody.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(badRequestBody);
        dos.flush();
    }
}
