package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class AnnotationServlet implements HttpServlet {
    private final Map<String, ControllerMethod> pathMap;

    public AnnotationServlet(List<Object> controllers) {
        this.pathMap = new HashMap<>();
        initializePathMap(controllers);
    }

    private void initializePathMap(List<Object> controllers) {
        for (Object controller : controllers) {
            for (Method method : controller.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    String path = method.getAnnotation(Mapping.class).value();
                    if (pathMap.containsKey(path)) {
                        ControllerMethod controllerMethod = pathMap.get(path);
                        throw new IllegalArgumentException("경로 중복 등록, path=" + path + ", method=" + method + ", 이미 등록된 메서드 입니당! =" + controllerMethod.method);
                    }
                    pathMap.put(path, new ControllerMethod(controller, method));
                }
            }
        }
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String path = request.getPath();
        ControllerMethod controllerMethod = pathMap.get(path);

        if (controllerMethod == null) {
            throw new PageNotFoundException("request=" + path);
        }
        controllerMethod.invoke(request, response);
    }

    private static class ControllerMethod {
        private final Object controller;
        private final Method method;

        public ControllerMethod(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        public void invoke(HttpRequest request, HttpResponse response) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i] == HttpRequest.class) {
                    args[i] = request;
                } else if (parameterTypes[i] == HttpResponse.class) {
                    args[i] = response;
                } else {
                    throw new IllegalArgumentException("무슨 타입을 넣은거지.." + parameterTypes[i]);
                }
            }

            if (parameterTypes.length == 1 && parameterTypes[0] == HttpResponse.class) {
                Mapping mapping = method.getAnnotation(Mapping.class);
                String path = mapping.value();
                String resourcePath = "static" + path + "/index.html";

                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                    if (inputStream == null) {
                        throw new PageNotFoundException("Page not found: " + resourcePath);
                    }

                    byte[] content = inputStream.readAllBytes();
                    response.setStatus(200);
                    response.setContentType("text/html; charset=UTF-8");
                    response.writeBody(content);
                    return;

                } catch (IOException e) {
                    throw new RuntimeException("정적 페이지 처리 실패: " + resourcePath, e);
                }
            }

            try {
                method.invoke(controller, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
