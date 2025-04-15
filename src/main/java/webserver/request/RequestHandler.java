package webserver.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Dispatcher;
import webserver.response.Response;
import webserver.response.ResponseHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Dispatcher dispatcher;

    public RequestHandler(Socket connectionSocket, Dispatcher dispatcher) {
        this.connection = connectionSocket;
        this.dispatcher = dispatcher;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = RequestParser.parseRequest(in);
            Response response = dispatcher.dispatchRequest(request);

            ResponseHandler.createResponse(request, out, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }
}
