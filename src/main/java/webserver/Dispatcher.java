package webserver;

import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpMethod;
import webserver.exception.HttpException;
import webserver.request.Request;
import webserver.resolver.ErrorResolver;
import webserver.resolver.MethodResolver;
import webserver.response.Response;

/**
 * HTTP 요청을 처리하는 디스패처 클래스입니다.
 * 이 클래스는 요청을 적절한 핸들러로 라우팅하고, 발생하는 예외를 처리합니다.
 */
public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * HTTP 요청을 처리하고 적절한 응답을 반환합니다.
     * 요청의 메소드와 경로에 따라 적절한 핸들러를 찾아 요청을 처리하고,
     * 발생하는 예외는 적절한 에러 핸들러로 처리합니다.
     *
     * @param request 처리할 HTTP 요청
     * @return 처리 결과에 대한 HTTP 응답
     * @throws FileNotFoundException 요청된 리소스를 찾을 수 없는 경우
     */
    public Response dispatchRequest(Request request) throws FileNotFoundException {
        try {
            HttpMethod method = HttpMethod.getMethod(request.getHttpMethod());
            return MethodResolver.getHandlerByPath(request.getRequestUrl(), method).handle(request);
        } catch (HttpException e) {
            return ErrorResolver.getHandlerByStatus(e.getStatus()).handle(request);
        }
    }
}