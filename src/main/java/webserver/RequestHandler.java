package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Dispatcher;
import webserver.request.HttpRequest;
import webserver.request.RequestParser;
import webserver.response.HttpResponse;
import webserver.response.ResponseHandler;
import webserver.response.ResponseSender;

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
            Dispatcher dispatcher = new Dispatcher(request);
            HttpResponse response = dispatcher.dispatch();
            logger.debug("HttpResponse dispatched: {}", response);

            ResponseSender sender = new ResponseSender(out);
            sender.sendResponse(response);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
