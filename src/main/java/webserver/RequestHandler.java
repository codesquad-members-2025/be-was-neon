package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.resolver.ResourceResolver;
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
            HttpResponse response = new HttpResponse(dos);

            HttpRequest request = new HttpRequest(br);
            ResourceResolver resourceResolver = new ResourceResolver(request, response);
            resourceResolver.resolve();
        } catch (IOException e) {
            logger.error("Error initializing streams: {}", e.getMessage());
        }
    }

}
