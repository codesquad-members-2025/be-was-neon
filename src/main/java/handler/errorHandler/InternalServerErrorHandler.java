package handler.errorHandler;

import static webserver.common.Constants.EMPTY;

import handler.Handler;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

/**
 * 500 Internal Server Error 에러를 처리하는 핸들러 클래스입니다.
 * 이 핸들러는 서버 내부 오류가 발생했을 때 처리합니다.
 */
public class InternalServerErrorHandler implements Handler {
    private final ResourceLoader resourceLoader;

    /**
     * InternalServerErrorHandler의 생성자입니다.
     *
     * @param resourceLoader 에러 페이지를 로드하기 위한 리소스 로더
     */
    public InternalServerErrorHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 500 Internal Server Error 에러를 처리합니다.
     * 500 에러 페이지를 로드하여 응답으로 반환합니다.
     *
     * @param request 처리할 HTTP 요청
     * @return 500 에러 페이지를 포함한 HTTP 응답
     */
    @Override
    public Response handle(Request request) {
        byte[] responseBody = resourceLoader.fileToBytes("/error/500.html", false);
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, responseBody, EMPTY);
    }
}
