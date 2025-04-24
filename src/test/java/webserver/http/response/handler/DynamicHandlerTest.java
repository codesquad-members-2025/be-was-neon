package webserver.http.response.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webserver.http.request.Request;
import webserver.http.response.Response;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class DynamicHandlerTest {

    @Test
    @DisplayName("회원가입 성공하고 루트 URL로 리다이렉트한다.")
    public void createUserSuccessTest() {
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/user/create");
        when(request.getRequestLine("method")).thenReturn("POST");
        when(request.getBody())
                .thenReturn(Map.of("userId", "testerId1",
                        "password", "1234",
                        "name", "테스터1",
                        "email", "tester1@examplemail.com"));
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("HTTP/1.1 302 Found");
        assertThat(responseMessage).contains("Location: /");
    }

    @Test
    @DisplayName("GET 요청으로 회원가입 시 회원가입이 실패하면서 400 에러가 발생한다.")
    public void createUserFailTest() {
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/user/create");
        when(request.getRequestLine("method")).thenReturn("GET");
        when(request.getBody())
                .thenReturn(Map.of("userId", "testerId2",
                        "password", "1234",
                        "name", "테스터2",
                        "email", "tester2@examplemail.com"));
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("HTTP/1.1 400 Bad Request");
    }

    @Test
    @DisplayName("testerId1과 1234로 로그인하면 로그인을 성공하고 루트 URL로 리다이렉트한다.")
    public void loginRedirectsToRootTest() {
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/create/user");
        when(request.getRequestLine("method")).thenReturn("GET");
        when(request.getBody())
                .thenReturn(Map.of("userId", "testerId1",
                        "password", "1234"));
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("HTTP/1.1 302 Found");
        assertThat(responseMessage).contains("Location: /");
    }

    @Test
    @DisplayName("testerId1과 1234로 로그인하면 로그인 성공하면 응답 쿠키 정보에 session-id가 저장된다.")
    public void loginCreatesSessionTest() {
        Request request = Mockito.mock(Request.class);
        when(request.getRequestLine("path")).thenReturn("/create/user");
        when(request.getRequestLine("method")).thenReturn("GET");
        when(request.getBody())
                .thenReturn(Map.of("userId", "testerId1",
                        "password", "1234"));
        DynamicHandler dynamicHandler = new DynamicHandler();
        Response response = dynamicHandler.handle(request);

        String responseMessage = new String(response.getResponseMessage());
        assertThat(responseMessage).contains("Set-Cookie");
        assertThat(responseMessage).contains("session-id=");
    }
}
