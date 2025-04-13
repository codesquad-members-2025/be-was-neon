package webserver.mapper;

import webserver.annotation.RequestMapping;
import handler.Handler;
import webserver.http.common.HttpMethod;
import webserver.http.exception.HttpException;
import webserver.resolver.DynamicHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static webserver.http.common.HttpConstants.SPACE;
import static webserver.http.response.HttpStatusCode.INTERNAL_SERVER_ERROR;

public class HandlerMapper {

    private static final HandlerMapper instance = new HandlerMapper();
    private final Map<String, DynamicHandler> mappings;

    private HandlerMapper() {
        this.mappings = new HashMap<>();

        // 컨트롤러 등록
        registerController(Handler.getInstance());
    }

    public static HandlerMapper getInstance() {
        return instance;
    }

    private void registerController(Object controller) {
        Method[] methods = controller.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                String key = mapping.method() + SPACE + mapping.path();
                DynamicHandler handler = (request) -> {
                    try {
                        return (webserver.resolver.ResolveResponse<?>) method.invoke(controller, request);
                    } catch (InvocationTargetException e) {
                        throw (HttpException) e.getCause();
                    } catch (IllegalAccessException e) {
                        throw new HttpException(INTERNAL_SERVER_ERROR);
                    }
                };
                mappings.put(key, handler);
            }
        }
    }

    public DynamicHandler getHandler(HttpMethod method, String path) {
        return mappings.get(method + SPACE + path);
    }

}
