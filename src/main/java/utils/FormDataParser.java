package utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FormDataParser {
    public static Map<String, String> parse(String queryString) {
        Map<String, String> params = new HashMap<String, String>();
        String[] queryParams = queryString.split("&");
        for (String param : queryParams) {
            String[] keyValue = param.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1
                    ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8).strip()
                    : "";
            params.put(key, value);
        }
        return params;
    }
}
