package webserver.resolver;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ContentTypeResolverTest {

    @ParameterizedTest
    @CsvSource({
            "index.html, text/html;charset=utf-8",
            "style.css, text/css",
            "script.js, application/javascript",
            "image.png, image/png",
            "photo.jpg, image/jpeg",
            "icon.jpeg, image/jpeg",
            "favicon.ico, image/x-icon",
            "logo.svg, image/svg+xml"
    })
    @DisplayName("확장자에 따른 Content-Type이 올바르게 반환되어야 한다.")
    void urlExtensionMatchContentTypeTest(String url, String expectedContentType) {
        // when
        String type = ContentTypeResolver.getContentType(url);

        // then
        assertThat(type).isEqualTo(expectedContentType);
    }


    @Test
    @DisplayName("지원하지 않는 확장자일 경우 기본 Content-Type을 반환한다.")
    void unknownExtensionReturnsDefaultContentType() {
        // given
        String url = "file.unknown";

        // when
        String type = ContentTypeResolver.getContentType(url);

        // then
        assertThat(type).isEqualTo("application/octet-stream");
    }

    @Test
    @DisplayName("확장자가 없는 url일 경우 기본 확장자(html)로 처리한다.")
    void noExtensionReturnsHtmlContentType() {
        // given
        String url = "index";

        // when
        String type = ContentTypeResolver.getContentType(url);

        // then
        assertThat(type).isEqualTo("text/html;charset=utf-8");
    }
}