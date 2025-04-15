package webserver.request;

import static webserver.common.Constants.BLANK;
import static webserver.common.Constants.COLON;
import static webserver.common.Constants.COMMA;
import static webserver.common.Constants.URL_IDX;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestParser {
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";
    private static final String QUESTION_MARK = "?";
    private static final String QUERY_DELIMITER = "\\?";
    private static final int PATH_INDEX = 0;
    private static final int QUERY_INDEX = 1;
    private static final int KEY_INDEX = 0;
    private static final int HEADER_IDX = 0;
    private static final int VALUE_IDX = 1;
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    private static final Set<String> commaSeperatedHeaders = Set.of(
            "Accept", "Accept-Encoding", "Accept-Language",
            "Cache-Control", "Connection", "Pragma",
            "TE", "Trailer", "Transfer-Encoding",
            "Upgrade", "Vary", "Via", "Warning"
    );

    public static Request parseRequest(InputStream in) throws IOException {
        Map<String, List<String>> requestMap = new HashMap<>();

        ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
        List<String> headerLines = new ArrayList<>();

        readInputStream(in, lineBuffer, headerLines);

        String requestLine = headerLines.getFirst();
        logger.debug("requestLine : {}", requestLine);

        String[] requestLineSplit = requestLine.split(BLANK);
        Map<String, String> queryMap = getQueryStringByRequestUrl(requestLineSplit[URL_IDX]);
        requestLineSplit[URL_IDX] = getRequestUrlByQueryString(requestLineSplit[URL_IDX]);

        for (int i = 1; i < headerLines.size(); i++) {
            parseRequestHeader(headerLines.get(i), requestMap);
            logger.debug("header : {}", headerLines.get(i));
        }

        Map<String, String> body = getBody(in, requestMap);

        return new Request(requestLineSplit, queryMap, requestMap, body);
    }

    private static void readInputStream(InputStream in, ByteArrayOutputStream lineBuffer, List<String> headerLines)
            throws IOException {
        int b;
        boolean lastCR = false;
        while ((b = in.read()) != -1) {
            if (b == '\r') {
                lastCR = true;
                continue;
            }
            if (b == '\n' && lastCR) {
                String line = lineBuffer.toString(StandardCharsets.UTF_8);
                if (line.isEmpty()) break; // 빈 줄: 헤더 끝
                headerLines.add(line);
                lineBuffer.reset();
                lastCR = false;
                continue;
            }
            lineBuffer.write(b);
            lastCR = false;
        }
    }

    private static Map<String, String> getBody(InputStream in, Map<String, List<String>> requestMap) throws IOException {
        String contentLength = requestMap.getOrDefault("Content-Length", List.of()).stream().findFirst().orElse("");
        Map<String, String> bodyMap = new HashMap<>();

        if (!contentLength.isEmpty() && Integer.parseInt(contentLength) > 0) {
            int length = Integer.parseInt(contentLength);
            byte[] bodyBytes = new byte[length];
            int bytesRead = 0;
            while (bytesRead < length) {
                int result = in.read(bodyBytes, bytesRead, length - bytesRead);
                if (result == -1) break;
                bytesRead += result;
            }
            String body = new String(bodyBytes, StandardCharsets.UTF_8);
            bodyMap = getQueryMap(body);
        }
        return bodyMap;
    }


    private static void parseRequestHeader(String line, Map<String, List<String>> requestMap) {
        String[] split = line.split(COLON, 2);
        String key = split[HEADER_IDX].trim();
        String value = split[VALUE_IDX].trim();

        if (commaSeperatedHeaders.contains(key)) {
            addCommaSeperatedValueToMap(requestMap, value, key);
        } else {
            addSingleValueToMap(requestMap, key, value);
        }
    }

    private static String getRequestUrlByQueryString(String requestUrl) {

        if (requestUrl.contains(QUESTION_MARK)){
            String[] splitUrl = requestUrl.split(QUERY_DELIMITER);
            requestUrl = splitUrl[PATH_INDEX];
        }
        return requestUrl;
    }

    private static Map<String, String> getQueryStringByRequestUrl(String requestUrl) {
        Map<String, String> queryMap = new HashMap<>();

        if (requestUrl.contains(QUESTION_MARK)){
            String[] splitUrl = requestUrl.split(QUERY_DELIMITER);
            String queryString = splitUrl[QUERY_INDEX];
            queryMap = getQueryMap(queryString);
        }
        return queryMap;
    }
    private static Map<String, String> getQueryMap(String queryString) {
        return Arrays.stream(queryString.split(AMPERSAND))
                .map(s -> s.split(EQUAL, 2))
                .collect(Collectors.toMap(s -> s[KEY_INDEX], s -> URLDecoder.decode(s[VALUE_IDX], StandardCharsets.UTF_8)));
    }

    private static void addSingleValueToMap(Map<String, List<String>> requestMap, String key, String value) {
        requestMap.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    private static void addCommaSeperatedValueToMap(Map<String, List<String>> requestMap, String values, String key) {
        for (String value : values.split(COMMA)) {
            addSingleValueToMap(requestMap, key, value);
        }
    }

}
