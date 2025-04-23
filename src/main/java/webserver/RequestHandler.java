package webserver;

import java.io.*;
import java.net.Socket;

import java.util.HashMap;

import db.Database;
import dispatcher.RequestDispatcher;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import static util.Parser.loggerParser;
import static util.Parser.requestPathParser;

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
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(new DataOutputStream(out));

            RequestDispatcher.dispatch(request, response);
            logger.info(request.getPath()+" "+request.getVersion()+" "+request.getMethod());
            logger.info(String.valueOf(request.getHeaders()));
            logger.info(String.valueOf(request.getQueryParams()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}

//todo 1. 상태 코드에 따른 에러처리 해주기
//todo 2. HttpRequest와 parser 분리
//todo 3. controller?