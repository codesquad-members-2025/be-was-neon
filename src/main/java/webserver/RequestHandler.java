package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.loader.ResourceLoader;

public class RequestHandler implements Runnable {
    public static final String HTTP_METHOD = "Method";
    public static final String REQUEST_URL = "Url";
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
            Map<String, List<String>> requestMap = requestParser.parseRequest(in);
            byte[] body = generateBody(requestMap);

            String type = ContentTypeResolver.getContentType(requestMap.get(REQUEST_URL).getFirst());
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length, type);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    private byte[] generateBody(Map<String, List<String>> requestMap) {
        byte[] body = new byte[0];
        if (requestMap.get(HTTP_METHOD).getFirst().equals(GET)) {
            body = resourceLoader.fileToBytes(requestMap.get(REQUEST_URL).getFirst());
        }
        return body;
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type + "\r\n");
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
