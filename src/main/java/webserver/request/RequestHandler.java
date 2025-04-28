package webserver.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Dispatcher;
import webserver.response.Response;
import webserver.response.ResponseHandler;

/**
 * 클라이언트의 HTTP 요청을 처리하는 핸들러 클래스입니다.
 * 이 클래스는 Runnable을 구현하여 별도의 스레드에서 실행될 수 있습니다.
 */
public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Dispatcher dispatcher;

    /**
     * RequestHandler의 생성자입니다.
     *
     * @param connectionSocket 클라이언트와의 연결을 나타내는 소켓
     * @param dispatcher 요청을 처리할 디스패처
     */
    public RequestHandler(Socket connectionSocket, Dispatcher dispatcher) {
        this.connection = connectionSocket;
        this.dispatcher = dispatcher;
    }

    /**
     * 클라이언트의 요청을 처리하는 메소드입니다.
     * 요청을 파싱하고, 디스패처를 통해 처리한 후, 응답을 클라이언트에게 전송합니다.
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = RequestParser.parseRequest(in);
            Response response = dispatcher.dispatchRequest(request);

            ResponseHandler.createResponse(request, out, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
