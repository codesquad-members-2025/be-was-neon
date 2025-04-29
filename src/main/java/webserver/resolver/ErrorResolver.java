package webserver.resolver;

import handler.Handler;
import handler.errorHandler.InternalServerErrorHandler;
import handler.errorHandler.MethodNotAllowedHandler;
import handler.errorHandler.NotFoundHandler;
import handler.errorHandler.UnauthorizedHandler;
import java.util.EnumMap;
import webserver.common.HttpStatus;
import webserver.loader.FileResourceLoader;
import webserver.loader.ResourceLoader;

/**
 * HTTP 상태 코드에 따라 적절한 에러 핸들러를 찾아주는 클래스입니다.
 * 각 HTTP 상태 코드에 대한 에러 페이지를 처리하는 핸들러를 관리합니다.
 */
public class ErrorResolver {
    private static final ResourceLoader RESOURCE_LOADER = new FileResourceLoader();
    private static final EnumMap<HttpStatus, Handler> ERROR_HANDLERS = new EnumMap<>(HttpStatus.class);

    static {
        // 에러 핸들러 등록
        for (ErrorMapping error : ErrorMapping.values()) {
            ERROR_HANDLERS.put(error.status, error.handler);
        }
    }

    /**
     * 주어진 HTTP 상태 코드에 해당하는 에러 핸들러를 반환합니다.
     * 만약 해당하는 에러 핸들러가 없는 경우 NotFoundHandler를 반환합니다.
     *
     * @param status HTTP 상태 코드
     * @return 해당하는 에러 핸들러
     */
    public static Handler getHandlerByStatus(HttpStatus status) {
        return ERROR_HANDLERS.getOrDefault(status, new NotFoundHandler(RESOURCE_LOADER));
    }

    /**
     * HTTP 상태 코드와 에러 핸들러를 매핑하는 enum 클래스입니다.
     * 각 상태 코드에 대한 에러 핸들러를 정의합니다.
     */
    private enum ErrorMapping {
        HANDLER_NOT_FOUND(HttpStatus.NOT_FOUND, new NotFoundHandler(RESOURCE_LOADER)),
        HANDLER_NOT_ALLOWED(HttpStatus.NOT_ALLOWED, new MethodNotAllowedHandler(RESOURCE_LOADER)),
        HANDLER_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, new InternalServerErrorHandler(RESOURCE_LOADER)),
        HANDLER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, new UnauthorizedHandler(RESOURCE_LOADER));

        private HttpStatus status;
        private Handler handler;

        /**
         * ErrorMapping의 생성자입니다.
         *
         * @param status HTTP 상태 코드
         * @param handler 해당 상태 코드를 처리할 에러 핸들러
         */
        ErrorMapping(HttpStatus status, Handler handler) {
            this.status = status;
            this.handler = handler;
        }
    }
}
