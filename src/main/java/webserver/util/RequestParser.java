package webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;


public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    public static HttpRequest parseRequest(InputStream in) throws IOException {
        String requestLine = readLine(in);
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

        Map<String, String> headers = parseHeader(in);
        String body = parseBody(in, headers);
        return new HttpRequest(requestLine, method, path, version, headers, parameters, body);
    }

    private static Map<String, String> parseHeader(InputStream in) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = readLine(in)) != null && !line.isEmpty()) {
            int index = line.indexOf(":");
            if (index != -1) {
                String name = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();
                headers.put(name, value);
            }
        }
        return headers;
    }

    public static Map<String, String> parseQuery(String queryString) throws UnsupportedEncodingException {
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

    private static String parseBody(InputStream in, Map<String, String> headers) throws IOException {
        if (!headers.containsKey("Content-Length")) {
            return "";
        }

        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        byte[] bodyBytes = new byte[contentLength];

        int totalRead = 0;
        while (totalRead < contentLength) {
            int read = in.read(bodyBytes, totalRead, contentLength - totalRead);
            if (read == -1) break; // 스트림 종료
            totalRead += read;
        }
        return new String(bodyBytes, 0, totalRead, "UTF-8");
    }

    private static String readLine(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int prev = -1, curr;
        while ((curr = in.read()) != -1) {
            if (prev == '\r' && curr == '\n') break;
            if (prev != -1) buffer.write(prev);
            prev = curr;
        }
        return buffer.toString("UTF-8");
    }



}
