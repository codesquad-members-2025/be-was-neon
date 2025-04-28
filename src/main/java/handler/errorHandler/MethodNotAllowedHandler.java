package handler.errorHandler;

import static webserver.common.Constants.EMPTY;

import handler.Handler;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

/**
 * 405 Method Not Allowed 에러를 처리하는 핸들러 클래스입니다.
 * 이 핸들러는 허용되지 않은 HTTP 메소드로의 요청을 처리합니다.
 */
public class MethodNotAllowedHandler implements Handler {
    private final ResourceLoader resourceLoader;

    /**
     * MethodNotAllowedHandler의 생성자입니다.
     *
     * @param resourceLoader 에러 페이지를 로드하기 위한 리소스 로더
     */
    public MethodNotAllowedHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 405 Method Not Allowed 에러를 처리합니다.
     * 405 에러 페이지를 로드하여 응답으로 반환합니다.
     *
     * @param request 처리할 HTTP 요청
     * @return 405 에러 페이지를 포함한 HTTP 응답
     */
    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes("/error/405.html", false);
        return new Response(HttpStatus.NOT_ALLOWED, responseBody, EMPTY);
    }
}
