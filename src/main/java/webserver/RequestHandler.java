package webserver;

import loader.ResourceData;
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

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (Socket conn = connection; InputStream in = conn.getInputStream(); OutputStream out = conn.getOutputStream()) {
            RequestHeader requestHeader =  RequestHeaderReader.readHeaders(in);
            ResponseBuilder responseBuilder = new ResponseBuilder(out);

            StaticResourceLoader staticResourceLoader = new StaticResourceLoader(requestHeader.getPath());
            ResourceData resourceData = staticResourceLoader.loadResourceData();

            responseBuilder.sendResponse(resourceData);
        } catch (IOException e) {
            logger.error("예외 발생 ", e);
        }
    }
}
