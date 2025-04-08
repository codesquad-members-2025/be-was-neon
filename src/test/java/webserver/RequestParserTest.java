package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestParserTest {

    @Test
    @DisplayName("request 헤더를 파싱한 결과가 요청과 일치해야한다.")
    void parsingRequestHeaderTest() throws IOException {
        // given
        String httpRequest =
                "GET /index.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "\r\n";

        InputStream input = new ByteArrayInputStream(httpRequest.getBytes());
        RequestParser parser = new RequestParser();

        // when
        String[] requestLine = parser.parseRequest(input);

        // then
        assertThat(requestLine[0]).isEqualTo("GET");
        assertThat(requestLine[1]).isEqualTo("/index.html");
        assertThat(requestLine[2]).isEqualTo("HTTP/1.1");
    }
}