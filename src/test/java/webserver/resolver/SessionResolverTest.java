package webserver.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.common.HttpHeaders;
import webserver.http.request.HttpRequest;
import webserver.http.request.RequestLine;

import static org.assertj.core.api.Assertions.assertThat;

class SessionResolverTest {

    @Test
    @DisplayName("HttpRequest Headers에 세션이 존재하면 세션을 주입한다")
    void 세션_존재시_주입_테스트() {
        // given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=1234567890");
        RequestLine requestLine = new RequestLine("GET / HTTP/1.1");
        HttpRequest request = new HttpRequest(requestLine, headers, null);

        // when
        SessionResolver.injectSession(request);

        // then
        assertThat(request.getSessionId()).isEqualTo("1234567890");
    }

    @Test
    @DisplayName("HttpRequest Headers에 세션이 존재하지 않으면 새로운 세션을 생성한다")
    void 세션_부재시_생성_테스트() {
        // given
        HttpHeaders headers = new HttpHeaders();
        RequestLine requestLine = new RequestLine("GET / HTTP/1.1");
        HttpRequest request = new HttpRequest(requestLine, headers, null);

        // when
        SessionResolver.injectSession(request);

        // then
        assertThat(request.getSessionId()).isNotNull();
    }

    @Test
    @DisplayName("HttpRequest에 새로운 세션이 생성되면 새로운 쿠키 추가 헤더를 추가한다")
    void 새로운_세션_쿠키_추가_테스트() {
        // given
        HttpHeaders headers = new HttpHeaders();
        RequestLine requestLine = new RequestLine("GET / HTTP/1.1");
        HttpRequest request = new HttpRequest(requestLine, headers, null);
        SessionResolver.injectSession(request);

        // when
        ResolveResponse<?> response = ResolveResponse.ok("Hello World");
        SessionResolver.addSessionCookieIfNew(request, response);

        // then
        assertThat(response.getHeaders().get("Set-Cookie")).isNotNull();
    }

    @Test
    @DisplayName("HttpRequest에 새로운 세션이 없으면 쿠키 추가 헤더를 추가하지 않는다")
    void 세션_이미_존재시_헤더에_추가_안함_테스트() {
        // given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=1234567890");
        RequestLine requestLine = new RequestLine("GET / HTTP/1.1");
        HttpRequest request = new HttpRequest(requestLine, headers, null);
        SessionResolver.injectSession(request);

        // when
        ResolveResponse<?> response = ResolveResponse.ok("Hello World");
        SessionResolver.addSessionCookieIfNew(request, response);

        // then
        assertThat(response.getHeaders().get("Set-Cookie")).isNull();
    }

}
