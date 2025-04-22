package webserver.http.request;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestLineParser {

    public Map<String, String> parseRequestLine(String[] requestLineParts) {
        Map<String, String> requestLine = new LinkedHashMap<>();
        requestLine.put("method", requestLineParts[0]);
        requestLine.put("path", parsePath(requestLineParts[1]));
        requestLine.put("protocol", requestLineParts[2]);
        return requestLine;
    }

    private String parsePath(String path) {
        if (isFile(path) && isExcludeQuery(path)) {
            return path;
        } else if (!isFile(path) && isExcludeQuery(path)) {
            return path + "/index.html";
        } else {
            String[] pathParts = path.split("\\?", 2);
            path = pathParts[0];
            return path;
        }
    }

    private boolean isFile(String path) {
        return path.contains(".");
    }

    private boolean isExcludeQuery(String path) {
        return !path.contains("?");
    }
}
