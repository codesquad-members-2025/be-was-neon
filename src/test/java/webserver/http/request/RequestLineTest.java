package webserver.http.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.http.exception.RequestParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestLineTest {

    @ParameterizedTest
    @CsvSource({
            "'GET /index.html HTTP/1.1', GET, /index.html, HTTP/1.1",
            "'POST /submit HTTP/1.0', POST, /submit, HTTP/1.0",
            "'PUT /update HTTP/2.0', PUT, /update, HTTP/2.0"
    })
    @DisplayName("쿼리파람 없는 올바른 메서드, URI, 프로토콜 및 헤더를 추출해야 한다.")
    void 정상_요청라인_파싱_테스트(String requestLine, String expectedMethod, String expectedPath, String expectedHttpVersion) {
        // When
        RequestLine requestLineObj = new RequestLine(requestLine);

        // Then
        assertThat(requestLineObj.getMethod().toString()).isEqualTo(expectedMethod);
        assertThat(requestLineObj.getPath()).isEqualTo(expectedPath);
        assertThat(requestLineObj.getHttpVersion()).isEqualTo(expectedHttpVersion);
        assertThat(requestLineObj.getQueryString()).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "GET /index.html HTTP/1.1 Host: localhost",
            "POST /submit HTTP/1.0 INVALID_HEADER",
            "PUT /update HTTP/2.0 MORE HEADERS",
            "DELETE /delete",
            "GET"
    })
    @DisplayName("잘못된 RequestLine 형식일 경우 RequestParseException 예외가 발생해야 한다.")
    void 잘못된_요청라인_예외_테스트(String requestLine) {
        // When & Then
        assertThatThrownBy(() -> new RequestLine(requestLine))
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Invalid request line: ");
    }

    @ParameterizedTest
    @CsvSource({
            "GET /index.html HTTP/3",
            "POST /submit HTTP/0",
            "PUT /update HTTP/2",
            "DELETE /delete HTTP/1"
    })
    @DisplayName("잘못된 HTTP 버전일 경우 RequestParseException 예외가 발생해야 한다.")
    void 잘못된_http_버전_예외_테스트(String requestLine) {
        // When & Then
        assertThatThrownBy(() -> new RequestLine(requestLine))
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Invalid HTTP version: ");
    }

    @Test
    @DisplayName("잘못된 Method 일 경우 RequestParseException 예외가 발생해야 한다.")
    void 잘못된_method__예외_테스트() {
        // Given
        String requestLine = "INVALID_METHOD /index.html HTTP/1.1";

        // When & Then
        assertThatThrownBy(() -> new RequestLine(requestLine))
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Invalid HTTP method: ");
    }

}
