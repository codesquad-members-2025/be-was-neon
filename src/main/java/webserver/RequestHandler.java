package webserver;

import Exceptions.ErrorResponder;
import Exceptions.HttpException;
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

        RequestReader requestReader= null;
        ResponseSender responseSender = null;

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            try {
                requestReader = new RequestReader(in);
                responseSender = new ResponseSender(out);

                Request request = requestReader.readRequest();

                Handler handler = Dispatcher.getHandler(request.getRequestHeader());
                handler.sendResponse(request, responseSender);
            } catch (HttpException e) {
                logger.warn("HTTP Exception Occurred - StatusCode: {}, StatusMessage: {}, ErrorMessage: {} ",
                        e.getStatus().getCode(), e.getStatus().getMessage(), e.getMessage());

                ErrorResponder.send(e, responseSender);
            } catch (Exception e) {
                // 예상 못한 예외
                logger.error("Unhandled Exception: ", e);
                ErrorResponder.send(e, responseSender);  // 내부 서버 오류
            }
        } catch (IOException e) {
            logger.warn("Error while handling request: {}", e.getMessage());
        }
    }
}
