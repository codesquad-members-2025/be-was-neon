package webserver.resolver;

import handler.Handler;
import handler.errorHandler.InternalServerErrorHandler;
import handler.errorHandler.MethodNotAllowedHandler;
import handler.errorHandler.NotFoundHandler;
import java.util.EnumMap;
import webserver.common.HttpStatus;
import webserver.loader.FileResourceLoader;
import webserver.loader.ResourceLoader;

public class ErrorResolver {
    private static final ResourceLoader RESOURCE_LOADER = new FileResourceLoader();
    private static final EnumMap<HttpStatus, Handler> ERROR_HANDLERS = new EnumMap<>(HttpStatus.class);

    static {
        // 에러 핸들러 등록
        for (ErrorMapping error : ErrorMapping.values()) {
            ERROR_HANDLERS.put(error.status, error.handler);
        }
    }

    public static Handler getHandlerByStatus(HttpStatus status) {
        return ERROR_HANDLERS.getOrDefault(status, new NotFoundHandler(RESOURCE_LOADER));
    }

    private enum ErrorMapping{
        HANDLER_NOT_FOUND(HttpStatus.NOT_FOUND, new NotFoundHandler(RESOURCE_LOADER)),
        HANDLER_NOT_ALLOWED(HttpStatus.NOT_ALLOWED, new MethodNotAllowedHandler(RESOURCE_LOADER)),
        Handler_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, new InternalServerErrorHandler(RESOURCE_LOADER));

        private HttpStatus status;
        private Handler handler;

        ErrorMapping(HttpStatus status, Handler handler) {
            this.status = status;
            this.handler = handler;
        }
    }
}
