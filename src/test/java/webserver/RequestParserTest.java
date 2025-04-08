package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
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
        Map<String, List<String>> requestMap = parser.parseRequest(input);

        // then
        assertThat(requestMap.get("Method").getFirst()).isEqualTo("GET");
        assertThat(requestMap.get("Url").getFirst()).isEqualTo("/index.html");
        assertThat(requestMap.get("Version").getFirst()).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName(",로 구분되는 헤더는 하나의 키에 각각 저장되어야 한다")
    void parsingCommaSeperatedTest() throws IOException {
        // given
        String httpRequest =
                "GET /index.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                        "\r\n";

        InputStream input = new ByteArrayInputStream(httpRequest.getBytes());
        RequestParser parser = new RequestParser();

        // when
        Map<String, List<String>> requestMap = parser.parseRequest(input);

        // then
        assertThat(requestMap.get("Accept").size()).isEqualTo(4);
        assertThat(requestMap.get("Accept")).containsExactly("text/html", "application/xhtml+xml", "application/xml;q=0.9", "*/*;q=0.8");
    }

    @Test
    @DisplayName(",로 구분되지 않는 헤더는 하나의 값으로 저장한다.")
    void parsingNotSeperatedTest() throws IOException {
        // given
        String httpRequest =
                "GET /index.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/134.0.0.0 Safari/537.36\r\n" +
                        "\r\n";

        InputStream input = new ByteArrayInputStream(httpRequest.getBytes());
        RequestParser parser = new RequestParser();

        // when
        Map<String, List<String>> requestMap = parser.parseRequest(input);

        // then
        assertThat(requestMap.get("User-Agent").size()).isEqualTo(1);
        assertThat(requestMap.get("User-Agent")).containsExactly("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");
    }
}