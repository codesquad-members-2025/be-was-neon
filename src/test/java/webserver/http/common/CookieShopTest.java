package webserver.http.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieShopTest {

    @Test
    @DisplayName("HttpHeaders에서 세션 아이디를 가져온다")
    void 헤더_세션_추출_테스트() {
        // given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID=1234567890");

        // when
        String sessionId = CookieShop.takeSessionIdFrom(headers);

        // then
        assertThat(sessionId).isEqualTo("1234567890");
    }

    @Test
    @DisplayName("HttpHeaders에서 쿠키값이 없으면 null을 반환한다")
    void 헤더_쿠키_없음_테스트() {
        // given
        HttpHeaders headers = new HttpHeaders();

        // when
        String sessionId = CookieShop.takeSessionIdFrom(headers);

        // then
        assertThat(sessionId).isNull();
    }

    @Test
    @DisplayName("sessionId를 주면 쿠키 Field 를 반환한다")
    void 쿠키_Field_파싱_테스트() {
        // given
        String sessionId = "1234567890";

        // when
        String cookie = CookieShop.bakeSessionCookie(sessionId);

        // then
        assertThat(cookie).isEqualTo("JSESSIONID=1234567890;Path=/;HttpOnly");
    }

}
