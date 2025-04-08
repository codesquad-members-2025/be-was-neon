package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestParser {
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

        while (!line.isEmpty()) {
            line = br.readLine();
            parseRequestHeader(line, requestMap);

            logger.debug("header : {}", line);
        }
        return requestMap;
    }

    private void parseRequestHeader(String line, Map<String, List<String>> requestMap) {
        int idx = line.indexOf(":");
        if (idx != -1) {
            String key = line.substring(0, idx).trim();
            String value = line.substring(idx + 1).trim();
            if (commaSeperatedHeaders.contains(key)) {
                addCommaSeperatedValueToMap(requestMap, value, key);
            } else {
                addSingleValueToMap(requestMap, key, value);
            }
        }

    }

    private void parseRequestLine(String line, Map<String, List<String>> requestMap) {
        String[] requestLine = line.split(" ");
        addSingleValueToMap(requestMap, "Method", requestLine[0]);
        addSingleValueToMap(requestMap, "Url", requestLine[1]);
        addSingleValueToMap(requestMap, "Version", requestLine[2]);
    }

    private void addSingleValueToMap(Map<String, List<String>> requestMap, String key, String value) {
        requestMap.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    private void addCommaSeperatedValueToMap(Map<String, List<String>> requestMap, String values, String key) {
        for (String value : values.split(",")) {
            addSingleValueToMap(requestMap, key, value);
        }
    }
}
