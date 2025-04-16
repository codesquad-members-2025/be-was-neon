package frontHandler;

import java.io.IOException;
import java.io.OutputStream;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelView handle(String method, String queryString, String body, Object handler) throws IOException;
}


