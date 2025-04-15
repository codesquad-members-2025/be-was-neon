package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.request.Request;
import webserver.request.RequestParser;

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

        // when
        Request request = RequestParser.parseRequest(input);

        // then
        assertThat(request.getHttpMethod()).isEqualTo("GET");
        assertThat(request.getRequestUrl()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("HTTP/1.1");
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

        // when
        Request request = RequestParser.parseRequest(input);

        // then
        assertThat(request.getHeaders().get("Accept").size()).isEqualTo(4);
        assertThat(request.getHeaders().get("Accept")).containsExactly("text/html", "application/xhtml+xml", "application/xml;q=0.9", "*/*;q=0.8");
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

        // when
        Request request = RequestParser.parseRequest(input);

        // then
        assertThat(request.getHeaders().get("User-Agent").size()).isEqualTo(1);
        assertThat(request.getHeaders().get("User-Agent")).containsExactly("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");
    }

    @Test
    @DisplayName("한글이 입력으로 들어오면 디코딩되어 저장된다.")
    void testQueryParameterParsing() throws IOException {
        // given
        String httpRequest =
                "GET /user/create?userId=test&name=%ED%99%8D%EA%B8%B8%EB%8F%99&email=test%40test.com HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "\r\n";

        InputStream input = new ByteArrayInputStream(httpRequest.getBytes());

        // when
        Request request = RequestParser.parseRequest(input);
        Map<String, String> queryMap = request.getQueryString();

        // then
        assertThat(queryMap.get("userId")).isEqualTo("test");
        assertThat(queryMap.get("name")).isEqualTo("홍길동");
        assertThat(queryMap.get("email")).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("특수 문자와 공백, 빈 값 등이 정상적으로 저장된다.")
    void testIllegalQueryParameter() throws IOException {
        // given
        String httpRequest =
                "GET /user/create?userId==hong&name=&email=%20hong%40test.com HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "\r\n";

        InputStream input = new ByteArrayInputStream(httpRequest.getBytes());

        // when
        Request request = RequestParser.parseRequest(input);
        Map<String, String> queryMap = request.getQueryString();

        // then
        assertThat(queryMap.get("userId")).isEqualTo("=hong");
        assertThat(queryMap.get("name")).isEqualTo("");
        assertThat(queryMap.get("email")).isEqualTo(" hong@test.com");
    }
}