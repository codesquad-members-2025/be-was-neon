package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.resolver.ContentTypeResolver;
import webserver.request.Request;

/**
 * HTTP 응답을 생성하고 클라이언트에게 전송하는 클래스입니다.
 * 이 클래스는 응답 헤더와 바디를 적절한 형식으로 구성하여 전송합니다.
 */
public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    /**
     * HTTP 요청에 대한 응답을 생성하고 전송합니다.
     * 요청의 URL에 따라 적절한 Content-Type을 설정하고,
     * 응답 헤더와 바디를 구성하여 클라이언트에게 전송합니다.
     *
     * @param request 클라이언트의 HTTP 요청
     * @param out 클라이언트에게 데이터를 전송할 출력 스트림
     * @param response 전송할 HTTP 응답
     */
    public static void createResponse(Request request, OutputStream out, Response response) {
        DataOutputStream dos = new DataOutputStream(out);
        String type = ContentTypeResolver.getContentType(request.getRequestUrl());

        String header = ResponseHeaderFactory.createHeader(type, response).toString();
        sendResponse(dos, response.getBody(), header);
    }

    /**
     * 응답 헤더와 바디를 클라이언트에게 전송합니다.
     *
     * @param dos 데이터를 전송할 DataOutputStream
     * @param body 전송할 응답 바디
     * @param header 전송할 응답 헤더
     */
    private static void sendResponse(DataOutputStream dos, byte[] body, String header) {
        try {
            dos.writeBytes(header);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
