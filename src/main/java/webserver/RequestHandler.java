package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.util.ContentTypeMapper;
import webserver.util.RequestParser;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in)); // in(inputstream)을 감싸서 텍스트 데이터를 줄 단위로 읽기
            DataOutputStream dos = new DataOutputStream(out); //out(outputstream)을 감싸서 데이터를 바이트 단위로 출력

            HttpRequest request = new HttpRequest(br); //HttpRequest 객체 생성
            String path = resolvePath(RequestParser.parseRequestPath(br)); // 요청 경로 파싱, 기본 경로 처리
            HttpResponse response = new HttpResponse(dos); //HttpReponse 객체 생성

            InputStream resource = getClass().getClassLoader().getResourceAsStream("static" + path);
            if (resource == null) {
                response.send404Response();
                return;  //이게 잘 이해ㅏㄱ 안가네 rewturn 왜 해줌?
            }

            byte[] body = resource.readAllBytes();
            response.send200Response(body, path);

            //요청라인과 헤더 출력
            logger.debug("Request Line: {}", request.getRequestLine());
            String filteredHeaders = RequestParser.extractFilteredHeaders(br);
            logger.debug(filteredHeaders);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //기본 경로 처리
    private String resolvePath(String path) {
        if (path == null || path.equals("/")) {
            return "/index.html";
        }
        return path;
    }
}
