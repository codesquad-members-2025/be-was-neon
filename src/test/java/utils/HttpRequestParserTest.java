package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestParserTest {

    HttpRequestParser httpRequestParser = new HttpRequestParser();

    @Test
    @DisplayName("HTTP 요청의 헤더를 받아 요청 url을 추출한다.")
    void parse_header_to_url() {
        String request = "GET /index.html HTTP/1.1";
        String url = httpRequestParser.parseUrl(request);
        assertThat(url).isEqualTo("/index.html");
    }
}
