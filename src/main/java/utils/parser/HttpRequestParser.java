package utils.parser;

import dto.HttpRequest;
import exception.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static domain.error.HttpClientError.findByStatusCode;

// HttpRequestParser.java
public class HttpRequestParser {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);

    public static HttpRequest parse(InputStream input) throws IOException, ClientException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        // 1. Request Line 파싱
        String requestLine = reader.readLine();
        if (requestLine == null) {
            throw new ClientException(findByStatusCode(400));
        }

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length < 3) {
            throw new ClientException(findByStatusCode(400));
        }

        // 2. 메서드/경로 추출
        String method = requestParts[0];
        String fullPath = requestParts[1];

        // 3. 경로/쿼리스트링 분리
        String[] pathParts = fullPath.split("\\?", 2);
        String path = pathParts[0];
        String queryString = (pathParts.length > 1) ? pathParts[1] : null;

        // 4. 헤더 파싱
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerEntry = line.split(":", 2);
            if (headerEntry.length == 2) {
                headers.put(headerEntry[0].trim().toLowerCase(),
                        headerEntry[1].trim());
            }
        }

        Map<String, String> cookies = new HashMap<>();
        if (headers.containsKey("cookie")) {
            String cookieHeader = headers.get("cookie");
            parseCookies(cookieHeader, cookies);
            log.info("Cookies: {}", cookies);
        }

        // 5. 본문 파싱
        String body = null;
        if ("POST".equalsIgnoreCase(method)) {
            int contentLength = Integer.parseInt(
                    headers.getOrDefault("content-length", "0")
            );
            if (contentLength > 0) {
                byte[] bufferd = new byte[contentLength];
                char[] buffer = new char[contentLength];
                reader.read(buffer, 0, contentLength);
                body = new String(buffer);
            }
        }

        return new HttpRequest(method, path, queryString, headers,cookies, body);
    }


    private static void parseCookies(String cookieHeader, Map<String, String> cookies) {
        String[] cookiePairs = cookieHeader.split(";");
        for (String pair : cookiePairs) {
            String[] keyValue = pair.trim().split("=", 2);
            if (keyValue.length == 2) {
                cookies.put(keyValue[0], keyValue[1]);
            }
        }
    }


}

