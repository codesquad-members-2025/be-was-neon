package handler;

import static webserver.common.Constants.EMPTY;

import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

/**
 * 정적 파일 요청을 처리하는 핸들러 클래스입니다.
 * 이 핸들러는 CSS, JavaScript, 이미지 등의 정적 파일을 제공합니다.
 */
public class StaticFileHandler implements Handler {
    private final ResourceLoader resourceLoader;

    /**
     * StaticFileHandler의 생성자입니다.
     *
     * @param resourceLoader 정적 파일을 로드하기 위한 리소스 로더
     */
    public StaticFileHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 정적 파일 요청을 처리합니다.
     * 요청된 파일을 로드하여 응답으로 반환합니다.
     *
     * @param request 정적 파일 요청
     * @return 요청된 파일을 포함한 HTTP 응답
     */
    @Override
    public Response handle(Request request) {
        logger.debug("request Url : {}", request.getRequestUrl());
        byte[] responseBody = resourceLoader.fileToBytes(request.getRequestUrl(), false);
        return new Response(HttpStatus.OK, responseBody, EMPTY);
    }
}
