package webserver;

import httpconst.HttpConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.RequestHeader;
import request.RequestReader;
import request.RequestRouter;
import request.RequestStatusLine;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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
            String statusLine = br.readLine();
            logger.debug("Request received: {}", statusLine);

            RequestReader requestReader = new RequestReader();
            RequestStatusLine requestStatusLine = requestReader.readStatusLine(statusLine);

            String request;
            Map<String, String> headers = new HashMap<>();
            while((request = br.readLine()) != null && !request.isEmpty()){
                String[] headersInfo = request.split(HttpConst.COLON_SPACE);
                headers.put(headersInfo[0], headersInfo[1]);
                // readheader 메서드 return requestHeader
                logger.debug("Request received: {}", request);
            }
            RequestHeader requestHeader = new RequestHeader(headers);

            // http 바디 파싱
            String contentLengthValue = requestHeader.getHeaders().get(HttpConst.CONTENT_LENGTH);
            String bodyString = "";

            if (contentLengthValue != null) {
                int contentLength = Integer.parseInt(contentLengthValue);
                char[] body = new char[contentLength];
                int read = br.read(body, 0, contentLength);
                bodyString = new String(body, 0, read);
                logger.debug("Request body: {}", bodyString);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            // request를 넘겨주기
            RequestRouter.handle(requestStatusLine, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
