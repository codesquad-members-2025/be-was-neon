package webserver.http.request;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestParser {

    private final InputStream in;
    private final RequestLineParser requestLineParser;
    private final QueryParser queryParser;

    public RequestParser(InputStream in) {
        this.in = in;
        this.requestLineParser = new RequestLineParser();
        this.queryParser = new QueryParser();
    }

    public Request parseRequest() throws IOException {

        String[] requestLineParts = readLine().split(" ");

        Map<String, String> requestLine = requestLineParser.parseRequestLine(requestLineParts);
        Map<String, String> headers = parseRequestHeaders();
        Map<String, String> queryMap = new LinkedHashMap<>();

        if (requestLineParts[1].contains("?")) {
            int queryStart = requestLineParts[1].indexOf('?');
            queryMap = queryParser.parseQuery(requestLineParts[1].substring(queryStart + 1));
        }

        return new Request(requestLine, headers, queryMap);
    }

    private Map<String, String> parseRequestHeaders() throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        String line;

        while (!(line = readLine()).isEmpty()) {
            String[] headerParts = parseLine(line);
            headers.put(headerParts[0], headerParts[1]);
        }

        return headers;
    }

    private static String[] parseLine(String line) {
        return line.split(":");
    }

    private String readLine() throws IOException {
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
