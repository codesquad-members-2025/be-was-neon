package frontHandler.adapter;

import frontHandler.HandlerAdapter;
import frontHandler.ModelView;
import handler.ReturnViewPathHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ReturnViewPathAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        // [!] 핸들러 타입 체크 로직 수정
        return (handler instanceof ReturnViewPathHandler);
    }

    @Override
    public ModelView handle(String method,
                            String queryString,
                            String body,
                            Object handler) throws IOException {

        ReturnViewPathHandler controller = (ReturnViewPathHandler) handler;

        // 1. 파라미터 맵 생성
        Map<String, String> paramMap = createParamMap(method, queryString, body);

        // 2. 모델 객체 생성
        Map<String, Object> model = new HashMap<>();

        // 3. 핸들러 실행 ➔ 뷰 이름 반환
        String viewName = controller.process(paramMap, model);

        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
    }

    private Map<String, String> createParamMap(String method,
                                               String queryString,
                                               String body) {
        Map<String, String> paramMap = new HashMap<>();

        // 쿼리 스트링 파싱 (GET)
        if (queryString != null) {
            parseKeyValuePairs(queryString, paramMap);
        }

        // 본문 파싱 (POST)
        if ("POST".equalsIgnoreCase(method) && body != null) {
            parseKeyValuePairs(body, paramMap);
        }

        return paramMap;
    }

    private void parseKeyValuePairs(String source,
                                    Map<String, String> paramMap) {
        String[] pairs = source.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    paramMap.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

