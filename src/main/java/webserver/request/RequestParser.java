package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public static final String HTTP_METHOD = "Method";
    public static final String REQUEST_URL = "Url";
    private static final String REQUEST_VERSION = "Version";
    public static final String COLON = ":";
    private static final String COMMA = ",";
    private static final String BLANK = " ";
    private static final int METHOD_IDX = 0;
    private static final int URL_IDX = 1;
    private static final int VERSION_IDX = 2;
    private static final int HEADER_IDX = 0;
    private static final int VALUE_IDX = 1;
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    private final Set<String> commaSeperatedHeaders = Set.of(
            "Accept", "Accept-Encoding", "Accept-Language",
            "Cache-Control", "Connection", "Pragma",
            "TE", "Trailer", "Transfer-Encoding",
            "Upgrade", "Vary", "Via", "Warning"
    );

    public Map<String, List<String>> parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        Map<String, List<String>> requestMap = new HashMap<>();

        String line = br.readLine();
        parseRequestLine(line, requestMap);
        logger.debug("requestLine : {}", line);

        while ((line = br.readLine()) != null && !line.isEmpty()) {
            parseRequestHeader(line, requestMap);
            logger.debug("header : {}", line);
        }
        return requestMap;
    }

    public Map<String, String> getQueryMap(Map<String, List<String>> requestMap) {
        String queryString = requestMap.get("queryString").getFirst();
        return Arrays.stream(queryString.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
    }

    private void parseRequestHeader(String line, Map<String, List<String>> requestMap) {
        String[] split = line.split(COLON);
        String key = split[HEADER_IDX].trim();
        String value = split[VALUE_IDX].trim();

        if (commaSeperatedHeaders.contains(key)) {
            addCommaSeperatedValueToMap(requestMap, value, key);
        } else {
            addSingleValueToMap(requestMap, key, value);
        }
    }

    private void parseRequestLine(String line, Map<String, List<String>> requestMap) {
        String[] requestLine = line.split(BLANK);
        String requestUrl = getRequestUrlByQueryString(requestMap, requestLine);

        addSingleValueToMap(requestMap, HTTP_METHOD, requestLine[METHOD_IDX]);
        addSingleValueToMap(requestMap, REQUEST_URL, requestUrl);
        addSingleValueToMap(requestMap, REQUEST_VERSION, requestLine[VERSION_IDX]);
    }

    private String getRequestUrlByQueryString(Map<String, List<String>> requestMap, String[] requestLine) {
        String requestUrl = requestLine[URL_IDX];

        if (requestLine[URL_IDX].contains("?")){
            String[] splitUrl = requestLine[URL_IDX].split("\\?");
            requestUrl = splitUrl[0];
            addSingleValueToMap(requestMap, "queryString", splitUrl[1]);
        }
        return requestUrl;
    }

    private void addSingleValueToMap(Map<String, List<String>> requestMap, String key, String value) {
        requestMap.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    private void addCommaSeperatedValueToMap(Map<String, List<String>> requestMap, String values, String key) {
        for (String value : values.split(COMMA)) {
            addSingleValueToMap(requestMap, key, value);
        }
    }

}
