package handler;

import http.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestHandler {
    private InputStream in;

    public RequestHandler(InputStream in) {
        this.in = in;
    }

    public Request handleRequest() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        Map<String, String> requestLine = parseRequestLine(bufferedReader);
        Map<String, String> headers = parseRequestHeaders(bufferedReader);
        return new Request(requestLine, headers);
    }

    private Map<String, String> parseRequestLine(BufferedReader bufferedReader) throws IOException {
        Map<String, String> requestLine = new LinkedHashMap<>();
        String[] requestLineParts = bufferedReader.readLine().split(" ");
        requestLine.put("method", requestLineParts[0]);
        requestLine.put("path", requestLineParts[1]);
        requestLine.put("protocol", requestLineParts[2]);
        return requestLine;
    }

    private Map<String, String> parseRequestHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        String line;

        while ((line = bufferedReader.readLine()) != null) {

            if (line.isEmpty()) break;

            String[] headerParts = parseLine(line);
            headers.put(headerParts[0], headerParts[1]);
        }

        return headers;
    }

    private String[] parseLine(String line) {
        return line.split(":");
    }
}
