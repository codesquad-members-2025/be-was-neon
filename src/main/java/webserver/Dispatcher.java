package webserver;

import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.HttpMethod;
import webserver.request.Request;
import webserver.resolver.MethodResolver;
import webserver.response.Response;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public Response dispatchRequest(Request request) throws FileNotFoundException {
        HttpMethod method = HttpMethod.getMethod(request.getHttpMethod());
        return MethodResolver.getHandlerByPath(request.getRequestUrl(), method).handle(request);
    }
}
//dispatch -> http메소드를 찾고 핸들러를 가져옴 -> httpmethod에 함수형으로 핸들러 실행함수를 가지고 있음 -> methodResolver에서 requestUrl로 핸들러 인스턴스를 가져옴