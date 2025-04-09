package webserver.http;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.exception.RequestParseException;
import webserver.http.request.HttpRequest;

import java.io.BufferedReader;
import java.io.StringReader;

class HttpRequestTest {

    @Test
    @DisplayName("정상 요청 문자열 파싱: 올바른 메서드, URI, 프로토콜 및 헤더를 추출해야 한다.")
    public void 헤더_정상_파싱_테스트() throws Exception {
        // Given
        String requestStr =
                """
                        GET /index.html HTTP/1.1\r
                        Host: localhost\r
                        User-Agent: TestAgent\r
                        \r
                        """;
        BufferedReader reader = new BufferedReader(new StringReader(requestStr));

        // When
        HttpRequest request = new HttpRequest(reader);

        // Then
        Assertions.assertThat(request.getMethod()).isEqualTo("GET");
        Assertions.assertThat(request.getUri()).isEqualTo("/index.html");
        Assertions.assertThat(request.getProtocol()).isEqualTo("HTTP/1.1");
        Assertions.assertThat(request.getHeaders())
                .containsEntry("Host", "localhost")
                .containsEntry("User-Agent", "TestAgent");
    }

    @Test
    @DisplayName("빈 요청 문자열 파싱 시: RequestParseException 예외가 발생해야 한다.")
    public void 헤더_파싱_예외_테스트() {
        // Given
        String requestStr = "\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(requestStr));

        // When & Then
        Assertions.assertThatThrownBy(() -> new HttpRequest(reader))
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Empty request line");
    }

}
