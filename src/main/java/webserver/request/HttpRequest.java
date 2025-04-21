package webserver.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {

    private String method;
    private String path;
    private String protocol;
    private final Map<String, List<String>> headers;
    private final Map<String, List<String>> parameters;
    private String body;

    public HttpRequest(String method,
                       String path,
                       String protocol,
                       Map<String, List<String>> headers,
                       Map<String, List<String>> parameters,
                       String body) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
        this.parameters = parameters;
        this.body = body;
    }

    public String getPath() {
        return path;
    }

    public Map<String, List<String>> getParameters() {
        Map<String, List<String>> copy = parameters.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Collections.unmodifiableList(e.getValue()) // value인 List를 불변으로
                ));

        return Collections.unmodifiableMap(copy); // Map을 불변으로
    }
}
