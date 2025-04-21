package webserver.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestParser {

    public static Request parseRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String[] requestLineParts = bufferedReader.readLine().split(" ");

        Map<String, String> requestLine = parseRequestLine(requestLineParts);
        Map<String, String> headers = parseRequestHeaders(bufferedReader);
        Map<String, String> queryMap = new LinkedHashMap<>();

        if (checkIncludeQuery(requestLineParts[1])) {
            queryMap = parseQuery(requestLineParts[1]);
        }

        return new Request(requestLine, headers, queryMap);
    }

    private static Map<String, String> parseRequestLine(String[] requestLineParts) {
        Map<String, String> requestLine = new LinkedHashMap<>();
        requestLine.put("method", requestLineParts[0]);
        requestLine.put("path", parsePath(requestLineParts[1]));
        requestLine.put("protocol", requestLineParts[2]);
        return requestLine;
    }

    private static String parsePath(String path) {
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

    private static Map<String, String> parseRequestHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        String line;

        while ((line = bufferedReader.readLine()) != null) {

            if (line.isEmpty()) break;

            String[] headerParts = parseLine(line);
            headers.put(headerParts[0], headerParts[1]);
        }

        return headers;
    }

    private static String[] parseLine(String line) {
        return line.split(":");
    }

    private static boolean isFile(String path) {
        return path.contains(".");
    }

    private static Map<String, String> parseQuery(String path) {
        Map<String, String> queryMap = new LinkedHashMap<>();
        int queryStart = path.indexOf('?');

        if (queryStart == -1) {
            return queryMap;
        }

        String queryString = path.substring(queryStart + 1);
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

    private static boolean checkIncludeQuery(String path) {
        return path.contains("?");
    }
}
