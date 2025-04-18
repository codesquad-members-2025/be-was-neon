package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.RequestRouter;

import java.io.*;
import java.net.Socket;
// nio 제거하기

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String request = br.readLine();
            String[] requestInfoList = request.split(" ");
            logger.debug("Request received: {}", request);

            while(!request.equals("")){
                request = br.readLine();
                logger.debug("Request received: {}", request);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            // 정적 파일 경로 설정 (예: index.html)
            String url = requestInfoList[1];
            RequestRouter.handle(url, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
