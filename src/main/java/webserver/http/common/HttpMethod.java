package webserver.http.common;

import webserver.http.exception.RequestParseException;

public enum HttpMethod {

    GET,
    POST,
    DELETE,
    PUT,
    PATCH;

    public static HttpMethod fromString(String method) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) {
                return httpMethod;
            }
        }

        throw new RequestParseException("Invalid HTTP method: " + method);
    }

}
