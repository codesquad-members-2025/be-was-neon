package webserver.common;

import java.util.List;

public enum HttpMethod {
    GET(List.of()),
    POST(List.of("/user/create")),
    PUT(List.of()),
    DELETE(List.of());
    private List<String> path;

    HttpMethod(List<String> path) {
        this.path = path;
    }

    public static HttpMethod getMethod(String method){
        return HttpMethod.valueOf(method);
    }

}
