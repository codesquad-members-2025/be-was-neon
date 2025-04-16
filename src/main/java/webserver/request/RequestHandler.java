package webserver.request;

import java.io.*;
import java.net.Socket;

import db.Database;
import webserver.http.ContentType;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.loader.FileResourceLoader;
import webserver.loader.ResourceLoader;
import webserver.response.ResponseWriter;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String DEFAULT_MAIN_PAGE = "/index.html";
    private final String GET = "GET";
    private final String REGISTRATION_ACTION = "/create";
    private final String ROOT_PATH = "/";
    private final int METHOD_INDEX = 0;
    private final int URL_INDEX = 1;

    private Socket connection;
    private final ContentType contentType = new ContentType();
    private final RequestParser parser = new RequestParser();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            ResponseWriter responseWriter = new ResponseWriter(out);

            String[] requestLine = parser.generateRequestLine(in);
            String method = requestLine[METHOD_INDEX];
            String urlPath = requestLine[URL_INDEX];

            // 회원가입
            if(method.equals(GET) && urlPath.startsWith(REGISTRATION_ACTION)) {
                User justJoinedUser = parser.parseRegistrationData(urlPath);
                Database.addUser(justJoinedUser);

                DataOutputStream dos = new DataOutputStream(out);
                responseWriter.send302Redirect(ROOT_PATH);
                return;
            }

            if(urlPath.equals(ROOT_PATH)) {
                urlPath = DEFAULT_MAIN_PAGE;
            }

            ResourceLoader loader = new FileResourceLoader();
            byte[] body = loader.fileToBytes(urlPath);
            responseWriter.send200(body.length, contentType.getContentType(urlPath), body);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
