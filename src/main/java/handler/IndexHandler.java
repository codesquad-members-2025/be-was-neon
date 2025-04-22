package handler;

import model.User;
import session.SessionManager;

import java.util.Map;

import static session.SessionManager.SESSION_COOKIE_NAME;

public class IndexHandler implements ReturnViewPathHandler{

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {

        String sessionId = paramMap.get(SESSION_COOKIE_NAME);
        User user = SessionManager.getUser(sessionId);

        // 모델에 사용자 정보 추가
        model.put("user", user);
        return "dynamic/index";
    }
}
