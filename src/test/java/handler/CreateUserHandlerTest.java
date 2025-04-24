package handler;

import static org.assertj.core.api.Assertions.*;

import db.Database;
import handler.auth.CreateUserHandler;
import java.util.HashMap;
import java.util.Map;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.common.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;

class CreateUserHandlerTest {

    @Test
    @DisplayName("reauest body의 요청대로 User가 만들어져야 한다.")
    void testHandle_CreatesUserAndReturnsRedirect() {
        // given
        CreateUserHandler handler = new CreateUserHandler();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userId", "testUser");
        requestBody.put("name", "테스터");
        requestBody.put("password", "pass123");
        requestBody.put("email", "test@example.com");

        String[] requestLine = new String[]{"POST", "/user/create", "HTTP/1.1"};
        Request mockRequest = new Request(requestLine, null, null, requestBody);

        // when
        Response response = handler.handle(mockRequest);
        User createdUser = Database.findUserById("testUser");

        // then
        assertThat(createdUser.getName()).isEqualTo("테스터");
        assertThat(createdUser.getPassword()).isEqualTo("pass123");
        assertThat(createdUser.getEmail()).isEqualTo("test@example.com");

        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getRedirectPath()).isEqualTo("/");
    }
}