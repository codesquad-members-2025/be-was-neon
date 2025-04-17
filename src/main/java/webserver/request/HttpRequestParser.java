package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);
    public static final char COLON = ':';
    public static final char QUESTION_MARK = '?';
    public static final String AND = "&";
    public static final char EQUAL = '=';

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        RequestLine requestLine = parseRequestLine(reader);
        Map<String, List<String>> headers = parseHeaders(reader);
        Map<String, List<String>> parameters = extractParameter(requestLine.path);

        return new HttpRequest(
                requestLine.method,
                requestLine.path,
                requestLine.protocol(),
                headers,
                parameters,
                null // request의 body는 아직 구현 안함.
        );
    }



    private static RequestLine parseRequestLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("Empty request line");
        }

        String[] requestLineArr = requestLine.split(" ");
        if (requestLineArr.length < 3) {
            throw new IllegalArgumentException("Invalid request line format");
        }
        logger.debug(requestLine);

        String method = requestLineArr[0];
        String path = requestLineArr[1].equals("/") ? "/index.html" : requestLineArr[1];
        String protocol = requestLineArr[2];
        return new RequestLine(method, path, protocol);
    }

    private static Map<String, List<String>> parseHeaders(BufferedReader reader) throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        String line;

        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int colonPos = line.indexOf(COLON);
            if (colonPos > 0) {
                String headerName = line.substring(0, colonPos).trim();
                String headerValue = line.substring(colonPos + 1).trim();

                logger.debug("{}: {}", headerName, headerValue);
                headers.computeIfAbsent(headerName, k -> new ArrayList<>()).add(headerValue);
            } else {
                throw new IllegalArgumentException("Invalid request headers format: missing colon");
            }
        }
        return headers;
    }

    private static Map<String, List<String>> extractParameter(String path) {
        HashMap<String, List<String>> parameters = new HashMap<>();
        int questionMarkPos = path.indexOf(QUESTION_MARK);
        if (questionMarkPos != -1) {
            String queryString = path.substring(questionMarkPos + 1);
            String[] pairs = queryString.split(AND);
            for (String pair : pairs) {
                int equalPos = pair.indexOf(EQUAL);
                if (equalPos < 0) {
                    throw new IllegalArgumentException("Invalid request parameters format: Missing equal");
                }

                String key = pair.substring(0, equalPos);
                String value = pair.substring(equalPos+1);

                key = HttpRequestUtils.decodeUrl(key);
                value = HttpRequestUtils.decodeUrl(value);

                parameters.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
        }
        return parameters;
    }

    private record RequestLine(String method, String path, String protocol) {
    }
}
