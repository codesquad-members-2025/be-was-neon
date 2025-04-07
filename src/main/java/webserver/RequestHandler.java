package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.loader.ResourceLoader;

public class RequestHandler implements Runnable {
    public static final int METHOD_INDEX = 0;
    public static final int URL_INDEX = 1;
    public static final String GET = "GET";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ResourceLoader resourceLoader;
    private final RequestParser requestParser;

    public RequestHandler(Socket connectionSocket, ResourceLoader resourceLoader, RequestParser requestParser) {
        this.connection = connectionSocket;
        this.resourceLoader = resourceLoader;
        this.requestParser = requestParser;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            String[] requestLine = requestParser.parseRequest(in);

            byte[] body = generateBody(requestLine);

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] generateBody(String[] requestLine) {
        byte[] body = new byte[0];
        if (requestLine[METHOD_INDEX].equals(GET)) {
            body = resourceLoader.fileToBytes(requestLine[URL_INDEX]);
        }
        return body;
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
}
