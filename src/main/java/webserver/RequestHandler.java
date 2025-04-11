package webserver;

import java.io.*;
import java.net.Socket;

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
            //클라이언트 header 분리
            String[] header = getRequestHeaders(in);
            logger.debug("Client Request (IP: {}, Port: {})\n{}",connection.getInetAddress(), connection.getPort(), String.join("\n", header));

            //클라이언트 requestLine 분리
            String[] requestLine = getRequestLine(header);
            String url = requestLine[1];

            //클라이언트 요청에 대한 응답처리
            byte[] body = getRequestedFileContent(url);
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "<h1>Hello World</h1>".getBytes();
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

    private String[] getRequestHeaders(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }

            sb.append(line).append("\r\n");
        }

        String header = sb.toString();
        return header.split("\r\n");
    }

    private String[] getRequestLine(String[] header) {
        return header[0].split(" ");
    }
}
