package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Request {
    private InputStream in;
    private Map<String, String> requestLine;
    private Map<String, String> headers;

    public Request(InputStream in) throws IOException {
        this.in = in;
        this.requestLine = new LinkedHashMap<>();
        this.headers = new LinkedHashMap<>();
        parseRequest();
    }

    private void parseRequest() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        parseRequestLine(bufferedReader);
        parseRequestHeaders(bufferedReader);
    }

    private void parseRequestLine(BufferedReader bufferedReader) throws IOException {
        String[] requestLineParts = bufferedReader.readLine().split(" ");
        requestLine.put("method", requestLineParts[0]);
        requestLine.put("path", requestLineParts[1]);
        requestLine.put("protocol", requestLineParts[2]);
    }

    private void parseRequestHeaders(BufferedReader bufferedReader) throws IOException {
        String line;

        while ((line = bufferedReader.readLine()) != null) {

            if (line.isEmpty()) break;

            String[] headerParts = parseLine(line);
            headers.put(headerParts[0], headerParts[1]);
        }
    }

    private String[] parseLine(String line) {
        return line.split(":");
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public String getRequestLine(String key) {
        return requestLine.get(key);
    }
}
