package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
import request.RequestReader;
import response.ResponseSender;
import response.handler.Handler;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (Socket conn = connection; InputStream in = conn.getInputStream(); OutputStream out = conn.getOutputStream()) {
            RequestReader requestReader = new RequestReader(in);
            ResponseSender responseSender = new ResponseSender(out);

            Request request = requestReader.readRequest();

            Handler handler = Dispatcher.getHandler(request.getRequestHeader());
            handler.sendResponse(request, responseSender);
        } catch (IOException e) {
            logger.error("예외 발생 ", e);
        }
    }
}
