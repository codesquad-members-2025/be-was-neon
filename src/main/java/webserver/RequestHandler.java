package webserver;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.util.RequestParser;

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
            DataOutputStream dos = new DataOutputStream(out); //out(outputstream)을 감싸서 데이터를 바이트 단위로 출력

            HttpRequest request = RequestParser.parseRequest(in); //HttpRequest 객체 생성 (br은 헤더용 In은 바디용으로 따로 넘김)
            HttpResponse response = new HttpResponse(dos); //HttpReponse 객체 생성

            Dispatcher dispatcher = new Dispatcher();
            Handler handler = dispatcher.getHandler(request);
            handler.handle(request, response);


            //요청라인과 헤더 출력
//            logger.debug("Request Line: {}", request.getRequestLine());
//            logger.debug("Headers: {}", request.getHeadersForLog());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
