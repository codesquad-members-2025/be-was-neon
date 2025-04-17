package webserver;

import java.io.*;
import java.net.Socket;

import http.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);

    private Socket connection;

    public DispatcherHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestHandler requestHandler = new RequestHandler(in);
            Request request = requestHandler.handleRequest();
            request.printRequest();
            String path = request.getRequestLine("path");
            ResponseHandler responseHandler = new ResponseHandler(out, path);
            responseHandler.sendResponse();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
