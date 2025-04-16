package webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;


public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    public static HttpRequest parseRequest(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("빈 요청입니다.");
        }

        String[] tokens = requestLine.split(" ");
        String method = tokens[0];
        String path = tokens[1];
        String version = tokens[2];

        Map<String, String> parameters = new HashMap<>(); //이러면 정적 요청도 빈 map을 항상 반환해주긴 함.. -> 자바 컬렉션 설뎨 원칙에서 대부분 값이 없을 땐 null이 아니라 빈 객체 반환 권장.
        int queryIndex = path.indexOf("?");
        if (queryIndex != -1) {
            String queryString = path.substring(queryIndex + 1);
            parameters = parseQuery(queryString);
            path = path.substring(0, queryIndex);
        }
        log.debug("Parsed Query Parameters: {}", parameters);

        Map<String, String> headers = parseHeader(br);
        return new HttpRequest(requestLine, method, path, version, headers, parameters);
    }

    private static Map<String, String> parseHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            int index = line.indexOf(":");
            if (index != -1) {
                String name = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();
                headers.put(name, value);
            }
        }
        return headers;
    }

    private static Map<String, String> parseQuery(String queryString) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx != -1) {
                String key = URLDecoder.decode(pair.substring(0, idx).trim(), "UTF-8");
                String value = URLDecoder.decode(pair.substring(idx + 1).trim(), "UTF-8");
                params.put(key, value);
            }
        }
        return params;
    }
}
