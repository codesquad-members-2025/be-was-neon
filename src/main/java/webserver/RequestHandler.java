package webserver;

import handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.*;
import response.HttpResponseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            RequestReader requestReader = new RequestReader(in);
            HttpResponseWriter responseWriter = new HttpResponseWriter(out);

            RequestStatusLine requestStatusLine = requestReader.readStatusLine();
            RequestHeader requestHeader = requestReader.readHeader();
            String body = requestReader.readBody(requestHeader);

            Request request = new Request(requestStatusLine, requestHeader, body);

            Handler handler = RequestRouter.route(request);
            handler.handle(request, responseWriter);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
