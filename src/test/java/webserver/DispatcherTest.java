package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.common.HttpHeaders;
import webserver.http.request.HttpRequest;
import webserver.http.request.RequestLine;
import webserver.http.response.HttpResponse;
import webserver.mapper.HandlerMapper;
import webserver.resolver.SessionResolver;
import webserver.session.SessionManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DispatcherTest {

    @BeforeEach
    void setUp() {
        // db 초기화 리플렉션
        try {
            Field usersField = Database.class.getDeclaredField("users");
            usersField.setAccessible(true);
            Map<?, ?> usersMap = (Map<?, ?>) usersField.get(null);
            usersMap.clear();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // 세션 초기화
        try {
            Field sessionManagerField = SessionManager.class.getDeclaredField("sessions");
            sessionManagerField.setAccessible(true);
            Map<?, ?> sessionManagerMap = (Map<?, ?>) sessionManagerField.get(null);
            sessionManagerMap.clear();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // HandlerMapper 초기화
        HandlerMapper.getInstance().initialize();
    }

    @Test
    @DisplayName("정적 리소스 요청에 올바른 요청시 200 OK 응답과 정적 리소스 바디를 반환한다.")
    void 올바른_정적_리소스_요청시_200_ok_와_바디를_반환_테스트() throws IOException {
        // given
        RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1");
        HttpRequest request = new HttpRequest(requestLine, null, null);
        Dispatcher dispatcher = new Dispatcher(request);

        // when
        HttpResponse dispatchResult = dispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(response).isNotNull();
        assertThat(response).contains("200 OK");
        assertThat(response).contains("<html>");
    }

    @Test
    @DisplayName("정적 리소스 요청시 없는 리소스에 대해 404 Not Found 응답을 반환한다.")
    void 없는_정적_리소스_요청시_404_not_found_응답을_반환_테스트() throws IOException {
        // given
        RequestLine requestLine = new RequestLine("GET /nonexistent.html HTTP/1.1");
        HttpRequest request = new HttpRequest(requestLine, null, null);
        Dispatcher dispatcher = new Dispatcher(request);

        // when
        HttpResponse dispatchResult = dispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(response).isNotNull();
        assertThat(response).contains("404 Not Found");
    }

    @Test
    @DisplayName("POST /create 요청시 유저가 정상적으로 생성되면 302 Found 응답을 반환한다.")
    void 유저_생성_정상_요청시_302_found_응답을_반환_테스트() throws IOException {
        // given
        RequestLine requestLine = new RequestLine("POST /create HTTP/1.1");
        String body = "userId=javajigi&name=자바지기&password=test&email=javajigi@naver.com";
        HttpRequest request = new HttpRequest(requestLine, null, body);
        Dispatcher dispatcher = new Dispatcher(request);

        // when
        HttpResponse dispatchResult = dispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(response).isNotNull();
        assertThat(response).contains("302 Found");
    }

    @Test
    @DisplayName("POST /create 요청시 유저가 이미 존재하면 409 Conflict 응답을 반환한다.")
    void 중복된_유저_생성_요청시_409_Conflict_반환_테스트() throws IOException {
        // given
        User user = new User("javajigi", "test", "자바지기", "javajigi@naver.com");
        Database.addUser(user);

        RequestLine requestLine = new RequestLine("POST /create HTTP/1.1");
        String body = "userId=javajigi&name=자바지기&password=test&email=javajigi@naver.com";
        HttpRequest request = new HttpRequest(requestLine, null, body);
        Dispatcher newDispatcher = new Dispatcher(request);

        // when
        HttpResponse dispatchResult = newDispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(response).isNotNull();
        assertThat(response).contains("409 Conflict");
    }

    @Test
    @DisplayName("POST /create 요청시 파라미터가 부족하거나 잘못된 경우 400 Bad Request 응답을 반환한다.")
    void 잘못된_생성_요청시_400_Bad_Request_반환_테스트() throws IOException {
        // given
        RequestLine requestLine = new RequestLine("POST /create HTTP/1.1");
        String body = "userId=javajigi&password=";
        HttpRequest request = new HttpRequest(requestLine, null, body);
        Dispatcher dispatcher = new Dispatcher(request);

        // when
        HttpResponse dispatchResult = dispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(response).isNotNull();
        assertThat(response).contains("400 Bad Request");
    }

    @Test
    @DisplayName("POST /login 요청시 유저가 정상적으로 로그인되면 302 Found 응답을 반환한다.")
    void 유저_로그인_정상_요청시_302_found_응답을_반환_테스트() throws IOException {
        // given
        User user = new User("javajigi", "test", "자바지기", "javajigi@naver.com");
        Database.addUser(user);

        RequestLine requestLine = new RequestLine("POST /login HTTP/1.1");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=1234567890");
        String body = "userId=javajigi&name=자바지기&password=test&email=javajigi@naver.com";
        HttpRequest request = new HttpRequest(requestLine, headers, body);
        Dispatcher dispatcher = new Dispatcher(request);

        // when
        HttpResponse dispatchResult = dispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(response).isNotNull();
        assertThat(response).contains("302 Found");
    }

    @Test
    @DisplayName("POST /login 요청시 유저가 존재하지 않으면 새로운 쿠키 생성 헤더가 없으며 400 Bad Request 응답을 반환한다.")
    void 존재하지_않는_유저_로그인_요청시_400_Bad_Request_반환_테스트() throws IOException {
        // given
        RequestLine requestLine = new RequestLine("POST /login HTTP/1.1");
        HttpHeaders headers = new HttpHeaders();
        String body = "userId=nonexistent&password=test";
        HttpRequest request = new HttpRequest(requestLine, headers, body);
        Dispatcher dispatcher = new Dispatcher(request);

        // when
        HttpResponse dispatchResult = dispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(response).isNotNull();
        assertThat(response).contains("400 Bad Request");
        assertThat(response).doesNotContain("Set-Cookie");
    }

    @Test
    @DisplayName("POST /login 요청시 파라미터가 부족하거나 잘못된 경우 새로운 쿠키 생성 헤더가 없으며 400 Bad Request 응답을 반환한다.")
    void 잘못된_로그인_요청시_400_Bad_Request_반환_테스트() throws IOException {
        // given
        User user = new User("javajigi", "test", "자바지기", "javajigi@naver.com");
        Database.addUser(user);

        RequestLine requestLine = new RequestLine("POST /login HTTP/1.1");
        HttpHeaders headers = new HttpHeaders();
        String body = "userId=javajigi&password=";
        HttpRequest request = new HttpRequest(requestLine, headers, body);
        Dispatcher dispatcher = new Dispatcher(request);

        // when
        HttpResponse dispatchResult = dispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(response).isNotNull();
        assertThat(response).contains("400 Bad Request");
        assertThat(response).doesNotContain("Set-Cookie");
    }

    @Test
    @DisplayName("POST /login 요청시 정상 로그인 후 세션에 유저 정보가 담기며 Set Cookie 헤더가 포함된다.")
    void 정상_로그인_요청시_쿠키_및_세션_정상_주입_테스트() throws IOException {
        // given
        User user = new User("javajigi", "test", "자바지기", "javajigi@naver.com");
        Database.addUser(user);

        // when
        RequestLine newRequestLine = new RequestLine("POST /login HTTP/1.1");
        String newBody = "userId=javajigi&password=test";
        HttpHeaders newHeaders = new HttpHeaders();
        HttpRequest newRequest = new HttpRequest(newRequestLine, newHeaders, newBody);
        SessionResolver.injectSession(newRequest);
        Dispatcher newDispatcher = new Dispatcher(newRequest);

        HttpResponse dispatchResult = newDispatcher.dispatch();
        String response = new String(dispatchResult.getBytes(), StandardCharsets.UTF_8);

        // then
        assertThat(newRequest.getSession()).isNotNull();
        assertThat(newRequest.getSession().getAttribute("user")).isNotNull();
        assertThat(response).contains("Set-Cookie: JSESSIONID=");
    }

}
