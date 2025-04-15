package webserver.http.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.exception.RequestParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestParserTest {

    @Test
    @DisplayName("Content-Length가 포함된 정상 요청: 200 OK와 올바른 본문 반환")
    void 정상_요청_ContentLength_테스트() throws IOException {
        // given
        String requestString =
                "GET /hello HTTP/1.1\r\n" +
                        "Host: example.com\r\n" +
                        "Content-Length: 5\r\n" +
                        "\r\n" +
                        "Hello";
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestParser parser = new RequestParser(reader);

        // when
        HttpRequest httpRequest = parser.parseRequest();

        // then
        assertThat(httpRequest.getRequestLine().toString()).isEqualTo("GET /hello HTTP/1.1");
        assertThat(httpRequest.getBody()).isEqualTo("Hello");
        assertThat(httpRequest.getHeaders().toString()).contains("example.com");
    }

    @Test
    @DisplayName("Chunked 인코딩 정상 요청: 올바른 본문 반환")
    void 정상_요청_Chunked_테스트() throws IOException {
        // given: Transfer-Encoding: chunked가 포함된 요청 문자열. 하나의 청크("Hello")와 마지막 청크("0")가 포함됨.
        String requestString =
                "GET /chunk HTTP/1.1\r\n" +
                        "Host: example.com\r\n" +
                        "Transfer-Encoding: chunked\r\n" +
                        "\r\n" +
                        "5\r\n" +
                        "Hello\r\n" +
                        "0\r\n" +
                        "\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestParser parser = new RequestParser(reader);

        // when
        HttpRequest httpRequest = parser.parseRequest();

        // then
        assertThat(httpRequest.getBody()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("잘못된 요청 라인: RequestParseException 발생")
    void 잘못된_요청라인_테스트() {
        // given: HTTP 버전이 누락되어 잘못된 요청 라인("GET /hello")을 가진 요청 문자열
        String requestString =
                "GET /hello\r\n" +
                        "\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestParser parser = new RequestParser(reader);

        // when & then
        assertThatThrownBy(parser::parseRequest)
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Invalid Request-Line");
    }

    @Test
    @DisplayName("잘못된 헤더 형식: RequestParseException 발생")
    void 잘못된_헤더형식_테스트() {
        // given
        String requestString =
                "GET /hello HTTP/1.1\r\n" +
                        "InvalidHeaderLine\r\n" +
                        "\r\n" +
                        "Hello";
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestParser parser = new RequestParser(reader);

        // when & then
        assertThatThrownBy(parser::parseRequest)
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Invalid header field");
    }

    @Test
    @DisplayName("CR 뒤 LF 누락: RequestParseException 발생")
    void CR뒤_LF_누락_테스트() {
        // given
        String requestString = "GET /hello HTTP/1.1\r";  // LF 누락
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestParser parser = new RequestParser(reader);

        // when & then
        assertThatThrownBy(parser::parseRequest)
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Invalid request line");
    }

    @Test
    @DisplayName("본문 길이가 부족한 경우: IOException 발생")
    void 본문_부족_ContentLength_테스트() {
        // given: Content-Length가 10으로 지정되어 있지만 실제 본문은 "Hello"(5글자)만 있는 요청 문자열
        String requestString =
                "GET /hello HTTP/1.1\r\n" +
                        "Content-Length: 10\r\n" +
                        "\r\n" +
                        "Hello";
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestParser parser = new RequestParser(reader);

        // when & then
        assertThatThrownBy(parser::parseRequest)
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Expected 10 bytes");
    }

    @Test
    @DisplayName("청크 데이터가 부족한 경우: RequestParseException 발생")
    void 청크데이터_부족_테스트() {
        // given: 청크 길이가 5로 지정되었으나 실제 청크 데이터는 "Hel"(3글자)만 제공하는 요청 문자열
        String requestString =
                "GET /chunk HTTP/1.1\r\n" +
                        "Transfer-Encoding: chunked\r\n" +
                        "\r\n" +
                        "5\r\n" +
                        "Hel\r\n" +
                        "0\r\n" +
                        "\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestParser parser = new RequestParser(reader);

        // when & then
        assertThatThrownBy(parser::parseRequest)
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Expected CRLF after chunk data");
    }

    @Test
    @DisplayName("청크 데이터 후 CRLF 누락: RequestParseException 발생")
    void 청크데이터_CRLF_누락_테스트() {
        // given: 청크 데이터 후 빈 CRLF(줄바꿈)가 누락된 잘못된 요청 문자열
        String requestString =
                "GET /chunk HTTP/1.1\r\n" +
                        "Transfer-Encoding: chunked\r\n" +
                        "\r\n" +
                        "5\r\n" +
                        "HelloX\r\n" +  // "Hello" 후에 CRLF로 인식되어야 할 부분에 "X" 문자가 남음
                        "0\r\n" +
                        "\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestParser parser = new RequestParser(reader);

        // when & then
        assertThatThrownBy(parser::parseRequest)
                .isInstanceOf(RequestParseException.class)
                .hasMessageContaining("Expected CRLF after chunk data");
    }

}
