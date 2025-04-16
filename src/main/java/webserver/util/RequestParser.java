package webserver.util;

import webserver.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    public static HttpRequest parseRequest(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("빈 요청입니다.");
        }

        String[] tokens = requestLine.split(" ");
        String method = tokens[0];
        String path = tokens[1];
        String version = tokens[2];

        Map<String, String> headers = new HashMap<>();
        String headerline;
        while ((headerline = br.readLine())  != null && !headerline.isEmpty()) {
            int index = headerline.indexOf(":");
            if (index != -1) {
                String headerName = headerline.substring(0, index).trim();
                String headerValue = headerline.substring(index + 1).trim();
                headers.put(headerName, headerValue);
            }
        }
        return new HttpRequest(requestLine, method, path, version, headers);
    }
}
