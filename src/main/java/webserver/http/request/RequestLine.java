package webserver.http.request;

import webserver.http.common.HttpMethod;
import webserver.http.exception.RequestParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RequestLine {

    private static final Pattern HTTP_VERSION_PATTERN = Pattern.compile("HTTP/\\d\\.\\d");
    private static final String QUERY_STRING_DELIMITER = "\\?";
    private static final String QUERY_STRING_DELIMITER_WITHOUT_ESCAPE = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";
    private static final String SPACE = " ";
    private static final String EMPTY = "";
    private static final int EXPECTED_TOKEN_COUNT = 3;

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> queryString;
    private final String httpVersion;

    public RequestLine(String requestLine) {
        String[] tokens = requestLine.split(SPACE);
        validateRequestLine(tokens);
        this.method = HttpMethod.fromString(tokens[0]);
        this.httpVersion = tokens[2];

        String[] uriParts = splitUri(tokens[1]);
        this.path = uriParts[0];
        this.queryString = parseQueryString(uriParts[1]);
    }

    private void validateRequestLine(String[] tokens) {
        if (tokens.length != EXPECTED_TOKEN_COUNT) {
            throw new RequestParseException("Invalid request line: " + String.join(" ", tokens));
        }
        if (!HTTP_VERSION_PATTERN.matcher(tokens[2]).matches()) {
            throw new RequestParseException("Invalid HTTP version: " + tokens[2]);
        }
    }

    private String[] splitUri(String uri) {
        if (uri.contains(QUERY_STRING_DELIMITER_WITHOUT_ESCAPE)) {
            return uri.split(QUERY_STRING_DELIMITER);
        }

        return new String[]{uri, EMPTY};
    }

    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParameters = new HashMap<>();
        if (queryString.isEmpty()) {
            return queryParameters;
        }

        String[] pairs = queryString.split(AMPERSAND);
        for (String pair : pairs) {
            String[] keyValue = pair.split(EQUAL);
            if (queryParameters.containsKey(keyValue[0])) {
                continue;
            }
            if (keyValue.length == 1) {
                queryParameters.put(keyValue[0], EMPTY);
                continue;
            }
            if (keyValue.length == 2) {
                queryParameters.put(keyValue[0], keyValue[1]);
                continue;
            }
            String joinedValue = java.util.Arrays
                    .stream(keyValue, 1, keyValue.length)
                    .collect(Collectors.joining(EQUAL));
            queryParameters.put(keyValue[0], joinedValue);
        }

        return queryParameters;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

}
