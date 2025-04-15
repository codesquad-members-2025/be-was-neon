package handler;

import exception.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

import static domain.error.HttpClientError.findByStatusCode;

public class RequestHandlerV2 implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerV2.class);
    private final Socket connection;
    private final StaticRequestHandler staticRequestHandler;
    private final UserRequestHandler userRequestHandler;

    public RequestHandlerV2(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.staticRequestHandler = new StaticRequestHandler();
        this.userRequestHandler = new UserRequestHandler();
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP: {}, Port: {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            // HTTP 요청 파싱
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String requestLine = reader.readLine();
            if (requestLine == null) return;

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                HttpResponseHelper.sendErrorResponse(out, new ClientException(findByStatusCode(400)));
                return;
            }

            String method = requestParts[0];
            String fullPath = requestParts[1];
            logger.debug("Request: {} {}", method, fullPath);

            // 경로와 쿼리스트링 분리
            String[] pathAndQuery = fullPath.split("\\?", 2);
            String path = pathAndQuery[0];
            String queryString = (pathAndQuery.length > 1) ? pathAndQuery[1] : null;

            // 라우팅 처리
            handleRouting(path, method, queryString, out);

        } catch (IOException e) {
            logger.info("IOException occur");
        }
    }

    private void handleRouting(String path, String method, String queryString, OutputStream out) throws IOException {
        if ("/create".equals(path) && "GET".equalsIgnoreCase(method)) {
            userRequestHandler.handleCreateUserRequest(queryString, out);
        } else {
            staticRequestHandler.handleStaticRequest(path, out);
        }
    }
}
