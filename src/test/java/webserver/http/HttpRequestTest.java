package webserver.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

class HttpRequestTest {
    String rawRequest;
    BufferedReader reader;
    HttpRequest request;

    @BeforeEach
    void init() throws IOException {
        rawRequest = "GET /hello?name=bazzi&age=20 HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "User-Agent: BazziTest\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n";

        reader = new BufferedReader(new StringReader(rawRequest));
        request = new HttpRequest(reader);
    }
    
    @Test
    @DisplayName("HTTP 요청라인이 잘 왔는지 확인")
    void testRequestLineParsing() throws Exception {
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/hello");
        assertThat(request.getParameter("name")).isEqualTo("bazzi");
        assertThat(request.getParameter("age")).isEqualTo("20");
    }

    @Test
    @DisplayName("HTTP 요청 헤더가 일치해야한다.")
    void testHeaderLineParsing() throws Exception {
        assertThat(request.getHeader("Host")).isEqualTo("localhost");
        assertThat(request.getHeader("User-Agent")).isEqualTo("BazziTest");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
    }

    @Test
    @DisplayName("POST 요청에 담긴 파라미터와 값이 일치해야한다.")
    void testBodyParsing() throws Exception {
        String body = "userId=bazzi&email=hi@test.com";
        byte[] bodyBytes = body.getBytes(UTF_8);
        int contentLength = bodyBytes.length + 10;

        String rawRequest =
                "POST /submit HTTP/1.1\r\n" +
                        "Content-Type: application/x-www-form-urlencoded\r\n" +
                        "Content-Length: " + contentLength + "\r\n" +
                        "\r\n" +
                        body;

        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
        assertThatThrownBy(() -> new HttpRequest(reader))
                .isInstanceOf(IOException.class)
                .hasMessageContaining(contentLength+"만큼 읽어야하는데");
    }
}