package webserver.http.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.common.HttpHeaders;
import webserver.http.common.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    @Test
    @DisplayName("HTTP 요청 객체가 올바르게 생성되어야 한다.")
    void 객체_생성_테스트() {
        // Given
        String requestLine = "GET /index.html HTTP/1.1";
        HttpHeaders headers = new HttpHeaders();
        String body = "Hello, World!";

        // When
        HttpRequest httpRequest = new HttpRequest(new RequestLine(requestLine), headers, body);

        // Then
        assertThat(httpRequest.getRequestLine().getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getRequestLine().getPath()).isEqualTo("/index.html");
        assertThat(httpRequest.getHeaders()).isEqualTo(headers);
        assertThat(httpRequest.getBody()).isEqualTo(body);
    }

}
