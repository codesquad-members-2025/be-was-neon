package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.RequestHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.assertj.core.api.Assertions.*;

public class RequestHandlerTest {

    // 내부 클래스
    static class TestSocket extends Socket {
        private final ByteArrayInputStream input;
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();

        public TestSocket(String httpRequest) {
            this.input = new ByteArrayInputStream(httpRequest.getBytes());
        }

        @Override
        public InputStream getInputStream() {
            return input;
        }

        @Override
        public OutputStream getOutputStream() {
            return output;
        }

        public String getResponse() {
            return output.toString();
        }
    }

    @Test
    @DisplayName("회원가입 발생 시 302 리다이렉트로 홈으로 가는지 테스트")
    void redirectToRootAfterRegistration() {
        // given
        String httpRequest = "GET /create?userId=hawoon724&name=윤하운&password=2222&email=hawoon724%40naver.com HTTP/1.1";
        TestSocket fakeSocket = new TestSocket(httpRequest);
        RequestHandler handler = new RequestHandler(fakeSocket);

        // when
        handler.run();

        // then
        String response = fakeSocket.getResponse();
        assertThat(response).contains("HTTP/1.1 302 Found ");
        assertThat(response).contains("Location: /");
    }
}
