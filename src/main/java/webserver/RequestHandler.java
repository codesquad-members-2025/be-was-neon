package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpRequestUtils;
import utils.HttpRequestParser;

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

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequest request = HttpRequestParser.parse(reader);

            HttpResponse response = new HttpResponse();
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = HttpRequestUtils.readFileBytes(request.getPath());
            response.response200Header(dos, body.length);
            response.responseBody(dos, body);
        } catch (IllegalArgumentException | IOException e) {
            logger.error(e.getMessage());
        }
    }
}
