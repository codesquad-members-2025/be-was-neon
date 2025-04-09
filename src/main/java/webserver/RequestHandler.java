package webserver;

import loader.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.RequestHeader;
import request.RequestHeaderReader;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private ResourceLoader resourceLoader;

    public RequestHandler(Socket connectionSocket, ResourceLoader resourceLoader) {
        this.connection = connectionSocket;
        this.resourceLoader = resourceLoader;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (Socket conn = connection; InputStream in = conn.getInputStream(); OutputStream out = conn.getOutputStream()) {
            RequestHeader requestHeader =  RequestHeaderReader.readHeaders(in);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = resourceLoader.loadResourceAsBytes(requestHeader.getPath());

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
}
