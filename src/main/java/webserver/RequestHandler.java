package webserver;

import loader.StaticResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.RequestHeader;
import request.RequestHeaderReader;
import response.ResponseBuilder;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private StaticResourceLoader staticResourceLoader;

    public RequestHandler(Socket connectionSocket, StaticResourceLoader staticResourceLoader) {
        this.connection = connectionSocket;
        this.staticResourceLoader = staticResourceLoader;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (Socket conn = connection; InputStream in = conn.getInputStream(); OutputStream out = conn.getOutputStream()) {
            RequestHeader requestHeader =  RequestHeaderReader.readHeaders(in);
            ResponseBuilder responseBuilder = new ResponseBuilder(out);

            byte[] body = staticResourceLoader.loadResourceAsBytes(requestHeader.getPath());

            responseBuilder.sendResponse(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
