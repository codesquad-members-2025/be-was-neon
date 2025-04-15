package webserver;

import java.io.*;
import java.net.Socket;

import db.Database;
import http.ContentType;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.loader.FileResourceLoader;
import webserver.loader.ResourceLoader;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String PATH = "src/main/resources/static";
    private final String DEFAULT_MAIN_PAGE = "/index.html";
    private final int METHOD_INDEX = 0;
    private final int URL_INDEX = 1;
    private final String GET = "GET";
    private final String REGISTRATION_ACTION = "/create";

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String[] requestLine = parser.generateRequestLine(in);
            String method = requestLine[METHOD_INDEX];
            String urlPath = requestLine[URL_INDEX];

            // 회원가입
            if(method.equals(GET) && urlPath.startsWith(REGISTRATION_ACTION)) {
                User justJoinedUser = parser.parseRegistrationData(urlPath);
                Database.addUser(justJoinedUser);

                DataOutputStream dos = new DataOutputStream(out);
                response302Redirect(dos, "/");
                return;
            }

            if(urlPath.equals("/")) {
                urlPath = DEFAULT_MAIN_PAGE;
            }

            ResourceLoader loader = new FileResourceLoader();
            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = loader.fileToBytes(urlPath);
            response200Header(dos, body.length, contentType.getContentType(urlPath));
            responseBody(dos, body);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            // flush() : 버퍼에 남아있는 데이터를 강제로 전송한다, 네트워크에 응답을 즉시 보내야 할 때 사용한다
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Redirect(DataOutputStream dos, String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        dos.writeBytes("\r\n");
    }
}
