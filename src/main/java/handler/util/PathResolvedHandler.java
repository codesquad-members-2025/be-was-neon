package handler.util;

import handler.Handler;
import java.util.Map;
import webserver.request.Request;
import webserver.response.Response;

public record PathResolvedHandler(Handler handler, Map<String, String> pathVariables) implements Handler{
    @Override
    public Response handle(Request request) {
        //Request에 Path Variable 추가
        Request requestWithPathVariable = new Request(request, pathVariables);
        // 실제 핸들러에게 위임
        return handler.handle(requestWithPathVariable);
    }
}
