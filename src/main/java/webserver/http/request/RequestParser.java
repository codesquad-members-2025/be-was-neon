package webserver.http.request;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestParser {

    private InputStream in;

    public RequestParser(InputStream in) {
        this.in = in;
    }

    public Request parseRequest() throws IOException {

        String[] requestLineParts = readLine(in).split(" ");

        Map<String, String> requestLine = parseRequestLine(requestLineParts);
        Map<String, String> headers = parseRequestHeaders();
        Map<String, String> queryMap = new LinkedHashMap<>();

        if (checkIncludeQuery(requestLineParts[1])) {
            queryMap = parseQuery(requestLineParts[1]);
        }

        return new Request(requestLine, headers, queryMap);
    }

    private Map<String, String> parseRequestLine(String[] requestLineParts) {
        Map<String, String> requestLine = new LinkedHashMap<>();
        requestLine.put("method", requestLineParts[0]);
        requestLine.put("path", parsePath(requestLineParts[1]));
        requestLine.put("protocol", requestLineParts[2]);
        return requestLine;
    }

    private String parsePath(String path) {
        if (isFile(path) && !checkIncludeQuery(path)) {
            return path;
        } else if (!isFile(path) && !checkIncludeQuery(path)) {
            return path + "/index.html";
        } else {
            String[] pathParts = path.split("\\?", 2);
            path = pathParts[0];
            return path;
        }
    }

    private Map<String, String> parseRequestHeaders() throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        String line;

        while (!(line = readLine(in)).isEmpty()) {
            String[] headerParts = parseLine(line);
            headers.put(headerParts[0], headerParts[1]);
        }

        return headers;
    }

    private static String[] parseLine(String line) {
        return line.split(":");
    }

    private boolean isFile(String path) {
        return path.contains(".");
    }

    private Map<String, String> parseQuery(String path) {
        Map<String, String> queryMap = new LinkedHashMap<>();
        int queryStart = path.indexOf('?');

        if (queryStart == -1) {
            return queryMap;
        }

        String queryString = path.substring(queryStart + 1);
        queryString = decodeQuery(queryString);
        String[] queryParameters = queryString.split("&");

        for (String queryParameter : queryParameters) {
            String[] keyValue = queryParameter.split("=", 2);
            if (keyValue.length == 2) {
                queryMap.put(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1) {
                // key만 있고 value는 없는 경우도 고려
                queryMap.put(keyValue[0], "");
            }
        }

        return queryMap;
    }

    private String decodeQuery(String query) {
        return URLDecoder.decode(query, StandardCharsets.UTF_8);
    }

    private boolean checkIncludeQuery(String path) {
        return path.contains("?");
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '\r') continue;
            if (c == '\n') break;
            sb.append((char) c);
        }
        return sb.toString();
    }
}
