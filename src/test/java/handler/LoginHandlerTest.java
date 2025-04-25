package handler;


import static org.assertj.core.api.Assertions.*;

import db.Database;
import handler.auth.LoginHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.common.HttpStatus;
import webserver.exception.NotRegisteredUserException;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;
import webserver.session.SessionManager;

class LoginHandlerTest {

    private final LoginHandler handler = new LoginHandler();

    @BeforeEach
    public void init() {
        User user = new User("testUser", "pass123", "테스터", "test@test.com");
        Database.addUser(user);
    }

    @AfterEach
    public void clearDB() {
        Database.deleteAllArticles();
        Database.deleteAll();
    }

    @Test
    @DisplayName("회원가입이 되어있고 userId와 password가 일치하면 로그인에 성공한다.")
    void loginSuccessTest() {
        //given
        Map<String, List<String>> headers = new HashMap<>();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userId", "testUser");
        requestBody.put("password", "pass123");

        String[] requestLine = new String[]{"POST", "/user/login", "HTTP/1.1"};
        Request mockRequest = new Request(requestLine, null, headers, requestBody);

        //when
        Response response = handler.handle(mockRequest);

        //then
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getCookie()).isNotNull();
        assertThat(response.getCookie()).contains("sessionId=");
    }

    @Test
    @DisplayName("회원가입이 되어있지 않다면 NotRegisteredUserException이 발생한다.")
    void NotRegisteredUserLoginTest() {
        //given
        Database.deleteAll();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userId", "testUser");
        requestBody.put("password", "pass123");

        String[] requestLine = new String[]{"POST", "/user/login", "HTTP/1.1"};
        Request mockRequest = new Request(requestLine, null, null, requestBody);

        //when & then
        assertThatThrownBy(() -> handler.handle(mockRequest))
                .isInstanceOf(NotRegisteredUserException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("비밀번호가 틀린경우 NotRegisteredUserException이 발생한다.")
    void wrongPasswordLoginTest() {
        //given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userId", "testUser");
        requestBody.put("password", "wrongPassword");

        String[] requestLine = new String[]{"POST", "/user/login", "HTTP/1.1"};
        Request mockRequest = new Request(requestLine, null, null, requestBody);

        //when & then
        assertThatThrownBy(() -> handler.handle(mockRequest))
                .isInstanceOf(NotRegisteredUserException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("기존 세션 id로 요청을 한 경우 같은 세션 객체를 반환한다.")
    void SameSessionIdTest() throws NoSuchFieldException, IllegalAccessException {
        //given
        //리플렉션으로 세션 설정
        SessionManager sessionManager = SessionManager.getInstance();
        Field field = sessionManager.getClass().getDeclaredField("sessionMap");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Session> map = (Map<String, Session>) field.get(sessionManager);
        map.put("testSessionId", new Session("testSessionId"));

        //쿠키 초기값 설정
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Cookie", List.of("sessionId=testSessionId"));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userId", "testUser");
        requestBody.put("password", "pass123");

        String[] requestLine = new String[]{"POST", "/user/login", "HTTP/1.1"};
        Request mockRequest = new Request(requestLine, null, headers, requestBody);

        //when
        Response response = handler.handle(mockRequest);
        Session session = sessionManager.getSession("testSessionId");
        User sessionUser = (User) session.getAttribute("sessionUser");

        //then
        assertThat(map.size()).isEqualTo(1);
        assertThat(session.getSessionId()).isEqualTo("testSessionId");
        assertThat(sessionUser.getUserId()).isEqualTo("testUser");
        assertThat(sessionUser.getPassword()).isEqualTo("pass123");
        assertThat(sessionUser.getName()).isEqualTo("테스터");
        assertThat(sessionUser.getEmail()).isEqualTo("test@test.com");

        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getCookie()).isNotNull();

        session.invalidate();
    }

}