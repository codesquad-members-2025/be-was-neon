package utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static constants.SpecialChars.*;

public class FormDataParser {
    public static Map<String, String> parse(String queryString) {
        Map<String, String> params = new HashMap<>();
        String[] queryParams = queryString.split(AMPERSAND);
        for (String param : queryParams) {
            String[] keyValue = param.split(EQUALS);
            String key = keyValue[0];
            String value = keyValue.length > 1
                    ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8).strip()
                    : EMPTY;
            params.put(key, value);
        }
        return params;
    }
}
