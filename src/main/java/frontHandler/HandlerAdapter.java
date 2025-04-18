package frontHandler;

import dto.HttpRequest;

import java.io.IOException;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelView handle(HttpRequest request, Object handler) throws IOException;
}


