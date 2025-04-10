package webserver.request;

import static webserver.request.RequestParser.HTTP_METHOD;
import static webserver.request.RequestParser.REQUEST_URL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.loader.ResourceLoader;
import webserver.response.ResponseHandler;

public class RequestHandler implements Runnable {
    private static final String GET = "GET";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ResourceLoader resourceLoader;
    private final RequestParser requestParser;
    private final ResponseHandler responseHandler;

    public RequestHandler(Socket connectionSocket, ResourceLoader resourceLoader, RequestParser requestParser, ResponseHandler responseHandler) {
        this.connection = connectionSocket;
        this.resourceLoader = resourceLoader;
        this.requestParser = requestParser;
        this.responseHandler = responseHandler;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Map<String, List<String>> requestMap = requestParser.parseRequest(in);
            byte[] body = generateBody(requestMap);

            responseHandler.createResponse(requestMap, out, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }


    private byte[] generateBody(Map<String, List<String>> requestMap) throws FileNotFoundException {
        byte[] body = new byte[0];
        if (requestMap.get(HTTP_METHOD).getFirst().equals(GET)) {
            body = resourceLoader.fileToBytes(requestMap.get(REQUEST_URL).getFirst());
        }
        return body;
    }

}
