package webserver.http.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(request.getHeaders())
                .containsEntry("Host", "localhost")
                .containsEntry("User-Agent", "TestAgent");
        assertThat(request.getRequestLine().getMethod().toString()).isEqualTo("GET");
        assertThat(request.getRequestLine().getPath()).isEqualTo("/index.html");
        assertThat(request.getRequestLine().getHttpVersion()).isEqualTo("HTTP/1.1");
    }

}
