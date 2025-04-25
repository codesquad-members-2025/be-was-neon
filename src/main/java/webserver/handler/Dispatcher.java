package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class Dispatcher {

    private final HttpRequest request;

    public Dispatcher(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse dispatch() {
        try {
            RouteTable method = RouteTable.from(request.getMethod());
            Handler handler = method.getHandler(request.getUrlPath());
            // 경로가 명시적으로 등록되지 않았는데 GET 일 때
            if (handler == null && method == RouteTable.GET) {
                handler = method.getHandler("*");
            }
            if (handler == null) return new NotFoundHandler().createNotFoundResponse();
            return handler.handle(request);
        } catch (IllegalArgumentException e) {
            return new NotFoundHandler().createNotFoundResponse();
        }
    }

    private boolean isStaticResource(String path) {
        return path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".svg") || path.endsWith(".jpeg") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".ico") || path.endsWith(".gif") || path.endsWith("woff2");
    }
}
