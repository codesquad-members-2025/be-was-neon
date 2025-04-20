package handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.common.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;
import webserver.session.SessionManager;

class LogoutHandlerTest {

    @Test
    @DisplayName("로그아웃을 하면 해당 세션이 삭제된다.")
    void logoutTest() throws NoSuchFieldException, IllegalAccessException {
        //given
        LogoutHandler handler = new LogoutHandler();

        //리플렉션으로 세션 설정
        SessionManager sessionManager = SessionManager.getInstance();
        Session session = new Session("testSessionId");
        session.setAttribute("sessionUser", new User("testUser", "pass123", "테스터", "test@test.com"));

        Field field = sessionManager.getClass().getDeclaredField("sessionMap");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Session> map = (Map<String, Session>) field.get(sessionManager);
        map.put("testSessionId", session);

        //쿠키 초기값 설정
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Cookie", List.of("sessionId=" + session.getSessionId()));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userId", "testUser");
        requestBody.put("password", "pass123");

        String[] requestLine = new String[]{"POST", "/user/login", "HTTP/1.1"};
        Request mockRequest = new Request(requestLine, null, headers, requestBody);

        //when
        Response response = handler.handle(mockRequest);

        //then
        assertThat(sessionManager.getSession("testSessionId")).isNotEqualTo(session); // 세션이 invalidate 됐는지
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getRedirectPath()).isEqualTo("/"); // 리디렉션 경로

    }

}