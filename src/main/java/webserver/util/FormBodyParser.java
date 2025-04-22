package webserver.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class FormBodyParser {
    public static Map<String, String> parseFormBody(String formBody) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        String[] pairs = formBody.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx != -1) {
                String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                params.put(key, value);
            }
        }
        return params;
    }
}
