package webserver.http.request;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
        Map<String, String> body = new LinkedHashMap<>();

        if (isIncludeBody(headers)) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            body = parseRequestBody(contentLength);
        }

        if (requestLineParts[1].contains("?")) {
            int queryStart = requestLineParts[1].indexOf('?');
            queryMap = queryParser.parseQuery(requestLineParts[1].substring(queryStart + 1));
        }

        return new Request(requestLine, headers, body, queryMap);
    }

    private Map<String, String> parseRequestHeaders() throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        String line;

        while (!(line = readLine()).isEmpty()) {
            String[] headerParts = parseLine(line);
            headers.put(headerParts[0], headerParts[1].trim());
        }

        return headers;
    }

    private Map<String, String> parseRequestBody(int contentLength) throws IOException {
        Map<String, String> body;

        byte[] readBody = readRequestBody(contentLength);

        String bodyString = new String(readBody, StandardCharsets.UTF_8);
        body = queryParser.parseQuery(bodyString);
        return body;
    }

    private byte[] readRequestBody(int contentLength) throws IOException {
        byte[] body = new byte[contentLength];

        int byteRead = 0;
        while (byteRead < contentLength) {
            int read = in.read(body, 0, contentLength);
            if (read == -1) break;
            byteRead += read;
        }

        return body;
    }

    private static String[] parseLine(String line) {
        return line.split(":");
    }

    private String readLine() throws IOException {
        StringBuilder lineBuilder = new StringBuilder();
        int readChar;
        while ((readChar = in.read()) != -1) {
            if (readChar == '\r') continue;
            if (readChar == '\n') break;
            lineBuilder.append((char) readChar);
        }
        return lineBuilder.toString();
    }

    private boolean isIncludeBody(Map<String, String> headers) {
        return headers.containsKey("Content-Length");
    }
}
