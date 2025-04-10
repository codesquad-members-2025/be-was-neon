package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestParserTest {
    @Test
    @DisplayName("HTTP 요청의 requestLine을 String 배열로 파싱할 수 있다.")
    void parse_requestLine() {
        String request = "GET /index.html HTTP/1.1";
        String[] requestLineParts = HttpRequestParser.parseRequestLine(request).get();
        assertThat(requestLineParts[0]).isEqualTo("GET");
        assertThat(requestLineParts[1]).isEqualTo("/index.html");
        assertThat(requestLineParts[2]).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("HTTP 요청의 requestLine의 양식이 잘못되었으면 Optional.empty를 반환한다.")
    void parse_invalid_requestLine() {
        String request = "GET /index.html";
        assertThat(HttpRequestParser.parseRequestLine(request)).isEmpty();
    }

    @Test
    @DisplayName("HTTP 요청의 requestHeader 한 줄을 받고, key와 value로 나누고 양 옆의 공백을 제거한 후 반환한다.")
    void parse_requestHeader() {
        String requestHeader = "Host: localhost:8080";
        assertThat(HttpRequestParser.parseRequestHeader(requestHeader).get()).containsExactly("Host", "localhost:8080");
    }

    @Test
    @DisplayName("HTTP 요청의 requestHeader의 양식이 잘못되었으면 Optional.empty를 반환한다.")
    void parse_invalid_requestHeader() {
        String requestHeader = "Host";
        assertThat(HttpRequestParser.parseRequestHeader(requestHeader)).isEmpty();
    }
}
