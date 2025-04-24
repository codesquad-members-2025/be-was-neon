package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final String COLON = ":";
    private static final String CONTENT_LENGTH = "content-length";

    public static HttpRequest parseRequest(InputStream in) throws IOException {
        ByteArrayOutputStream line = new ByteArrayOutputStream();
        List<String> headerLines = new ArrayList<>();

        readInputStream(in, line, headerLines);

        String requestLine = headerLines.getFirst();
        String[] requestParts = requestLine.split(" ");
        if (requestParts.length < 3) throw new IllegalArgumentException("Invalid request line: " + requestLine);
        logger.debug("request line : {}", requestLine);

        Map<String, String> headers = parseHeader(headerLines);
        String body = getBody(in, headers);

        return new HttpRequest(requestParts, headers, body);
    }

    public static void readInputStream(InputStream in, ByteArrayOutputStream line, List<String> headerLines) throws IOException {
        int read;
        boolean isLastCR = false;
        while ((read = in.read()) != -1) {
            if (read == '\r') {
                isLastCR = true;
                continue;
            }
            if (read == '\n' && isLastCR) {
                String singleLine = line.toString(StandardCharsets.UTF_8);
                if (singleLine.isEmpty()) break;
                headerLines.add(singleLine);
                line.reset();
                isLastCR = false;
                continue;
            }
            line.write(read);
            isLastCR = false;
        }
    }

    private static Map<String, String> parseHeader(List<String> headerLines) {
        Map<String, String> headers = new HashMap<>();

        for (int i = 1; i < headerLines.size(); i++) {
            String[] pair = headerLines.get(i).split(COLON, 2);
            if (pair.length < 2) {
                logger.warn("잘못된 헤더 형식: {}", headerLines.get(i));
                continue;
            }
            String key = pair[KEY_INDEX].trim().toLowerCase();
            String value = pair[VALUE_INDEX].trim();
            headers.put(key, value);
            logger.debug("header line: {} -> {}", key, value);
        }
        return headers;
    }

    private static String getBody(InputStream in, Map<String, String> headers) throws IOException {
        String contentLength = headers.getOrDefault(CONTENT_LENGTH, "");
        String body = "";

        if (!contentLength.isEmpty()) {
            try {
                int length = Integer.parseInt(contentLength);
                byte[] rawBody = new byte[length];
                int readLength = 0;
                while (readLength < length) {
                    int result = in.read(rawBody, readLength, length - readLength);
                    if (result == -1) break;
                    readLength += result;
                }
                body = new String(rawBody, StandardCharsets.UTF_8);
                logger.debug("body: {}", body);
            } catch (NumberFormatException e) {
                logger.warn("유효하지 않은 Content-Length 값: {}", contentLength);
            }
        }
        return body;
    }

    public static Map<String, String> parseQueryString(String body) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                map.put(URLDecoder.decode(keyValue[KEY_INDEX], StandardCharsets.UTF_8), URLDecoder.decode(keyValue[VALUE_INDEX], StandardCharsets.UTF_8));
            }
        }
        return map;
    }
}
