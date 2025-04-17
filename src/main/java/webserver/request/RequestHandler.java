package webserver.request;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.response.ResponseHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final RequestParser parser = new RequestParser();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest request = RequestParser.parseRequest(in);
            ResponseHandler responseHandler = new ResponseHandler(request, out);
            responseHandler.dispatch();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
