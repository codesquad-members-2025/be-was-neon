package frontHandler;

import java.io.IOException;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelView handle(String method, String queryString, String body, Object handler) throws IOException;
}


