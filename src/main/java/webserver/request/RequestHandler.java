package webserver.request;

import static webserver.common.Constants.EMPTY;
import static webserver.common.Constants.HTTP_METHOD;
import static webserver.common.Constants.REQUEST_URL;

import db.Database;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.List;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.response.Response;
import webserver.response.ResponseHandler;

public class RequestHandler implements Runnable {
    private static final String GET = "GET";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ResourceLoader resourceLoader;

    public RequestHandler(Socket connectionSocket, ResourceLoader resourceLoader) {
        this.connection = connectionSocket;
        this.resourceLoader = resourceLoader;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Map<String, List<String>> requestMap = RequestParser.parseRequest(in);
            Response response = generateBody(requestMap);

            ResponseHandler.createResponse(requestMap, out, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }


    private  Response generateBody(Map<String, List<String>> requestMap) throws FileNotFoundException {
        byte[] body = new byte[0];
        if (requestMap.get(HTTP_METHOD).getFirst().equals(GET)) {
            if (requestMap.get(REQUEST_URL).getFirst().equals("/user/create")){
                Map<String, String> queryMap = RequestParser.getQueryMap(requestMap);

                Database.addUser(new User(queryMap.get("userId"), queryMap.get("name"), queryMap.get("password"), queryMap.get("email")));
                return new Response(HttpStatus.HTTP_302,  body, "/");
            }
            body = resourceLoader.fileToBytes(requestMap.get(REQUEST_URL).getFirst());
            return new Response(HttpStatus.HTTP_200, body, EMPTY);
        }
        return new Response(HttpStatus.HTTP_404, body, EMPTY);
    }
}
