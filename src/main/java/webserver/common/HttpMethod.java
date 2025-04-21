package webserver.common;

import java.util.List;
import webserver.exception.MethodNotAllowedException;

public enum HttpMethod {
    GET, POST, PUT, DELETE;

    public static HttpMethod getMethod(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            throw new MethodNotAllowedException("지원하지 않는 HTTP 메소드: " + method);
        }
    }
}
