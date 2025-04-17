package webserver.mapper;

import handler.Handler;
import webserver.annotation.RequestMapping;
import webserver.http.common.HttpMethod;
import webserver.http.common.HttpSession;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;
import webserver.resolver.DynamicHandler;
import webserver.resolver.ResolveResponse;
import webserver.resolver.SessionResolver;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static webserver.http.common.HttpConstants.SPACE;
import static webserver.http.response.HttpStatusCode.INTERNAL_SERVER_ERROR;

public class HandlerMapper {

    private static final HandlerMapper instance = new HandlerMapper();
    // 잦은 리플렉션 사용으로 성능 저하를 방지하기 위해 MethodHandles.Lookup을 사용
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();
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
                registerMapping(controller, method);
            }
        }
    }

    private void registerMapping(Object controller, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        validateSignature(method);
        String key = mapping.method() + SPACE + mapping.path();

        try {
            MethodHandle methodHandle = lookup.unreflect(method).bindTo(controller);
            DynamicHandler handler = createDynamicHandler(method, methodHandle);
            mappings.put(key, handler);
        } catch (IllegalAccessException e) {
            throw new HttpException(INTERNAL_SERVER_ERROR);
        }
    }

    private void validateSignature(Method method) {
        for (Class<?> p : method.getParameterTypes()) {
            if (p != HttpRequest.class && p != HttpSession.class) {
                throw new IllegalStateException(
                        "[ERROR] 지원하지 않는 파라미터 타입: "
                                + method.getName() + " -> " + p.getSimpleName()
                );
            }
        }
    }

    private DynamicHandler createDynamicHandler(Method method, MethodHandle methodHandle) {
        return (request) -> {
            Object[] args = buildArguments(method, request);
            try {
                return (ResolveResponse<?>) methodHandle.invokeWithArguments(args);
            } catch (Throwable t) {
                if (t instanceof HttpException) {
                    throw (HttpException) t;
                }

                throw new HttpException(INTERNAL_SERVER_ERROR);
            }
        };
    }

    private Object[] buildArguments(Method method, HttpRequest request) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] == HttpRequest.class) {
                args[i] = request;
                continue;
            }

            if (paramTypes[i] == HttpSession.class) {
                SessionResolver.injectSession(request);
                args[i] = request.getSession();
            }
        }

        return args;
    }

    public DynamicHandler getHandler(HttpMethod method, String path) {
        return mappings.get(method + SPACE + path);
    }

}
