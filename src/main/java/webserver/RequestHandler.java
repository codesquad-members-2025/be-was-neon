package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.exception.RequestParseException;
import webserver.http.request.ABNFRequestParser;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            ABNFRequestParser requestParser = new ABNFRequestParser(br);
            HttpRequest request = requestParser.parseRequest();
            logger.debug("Request: {}", request);

            Dispatcher dispatcher = new Dispatcher(request);
            HttpResponse response = dispatcher.dispatch();
            dos.writeBytes(response.toString());
            dos.flush();
            logger.debug("Response: {}", response);
        } catch (IOException e) {
            logger.error("Error initializing streams: {}", e.getMessage());
        } catch (RequestParseException e) {
            logger.error("Error parsing request: {}", e.getMessage());
        }
    }

}
