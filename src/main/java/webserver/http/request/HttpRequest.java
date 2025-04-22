package webserver.http.request;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {

    private final String method;
    private final String path;
    private final String protocol;
    private final Map<String, List<String>> headers;
    private final Map<String, List<String>> queryParameters;
    private final Map<String, List<String>> bodyParameters;
    private final String body;

    public HttpRequest(String method,
                       String path,
                       String protocol,
                       Map<String, List<String>> headers,
                       Map<String, List<String>> queryParameters,
                       Map<String, List<String>> bodyParameters,
                       String body) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
        this.queryParameters = queryParameters;
        this.bodyParameters = bodyParameters;
        this.body = body;
    }

    public String getPath() {
        return path;
    }

    public Map<String, List<String>> getQueryParameters() {
        Map<String, List<String>> copy = queryParameters.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Collections.unmodifiableList(e.getValue()) // value인 List를 불변으로
                ));

        return Collections.unmodifiableMap(copy); // Map을 불변으로
    }
    public Map<String, List<String>> getBodyParameters() {
        Map<String, List<String>> copy = bodyParameters.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Collections.unmodifiableList(e.getValue()) // value인 List를 불변으로
                ));

        return Collections.unmodifiableMap(copy); // Map을 불변으로
    }

    public String getMethod() {
        return method;
    }
}
