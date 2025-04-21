package webserver.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static webserver.http.common.HttpConstants.*;

public class QueryStringParser {

    public static Map<String, String> parse(String input) {
        Map<String, String> queryParameters = new HashMap<>();
        if (input == null || input.isEmpty()) {
            return queryParameters;
        }

        String decodedQueryString = URLDecoder.decode(input, StandardCharsets.UTF_8);
        String[] pairs = decodedQueryString.split(AMPERSAND);
        for (String pair : pairs) {
            String[] keyValue = pair.split(EQUALS);
            // 이미 존재하는 키는 무시
            if (queryParameters.containsKey(keyValue[0])) {
                continue;
            }
            // 키는 존재하지만 값이 없는 경우 값은 빈 문자열
            if (keyValue.length == 1) {
                queryParameters.put(keyValue[0], EMPTY);
                continue;
            }
            // 키와 값이 모두 존재하는 경우
            if (keyValue.length == 2) {
                queryParameters.put(keyValue[0], keyValue[1]);
                continue;
            }
            // 키가 존재하고 값이 여러 개인 경우 가장 앞의 "=" 이후의 값은 모두 값으로 사용
            String joinedValue = java.util.Arrays
                    .stream(keyValue, 1, keyValue.length)
                    .collect(Collectors.joining(EQUALS));
            queryParameters.put(keyValue[0], joinedValue);
        }

        return queryParameters;
    }

}
