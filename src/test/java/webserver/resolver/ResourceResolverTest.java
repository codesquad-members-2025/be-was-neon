package webserver.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import provider.RequestBuilder;
import webserver.http.exception.HttpException;
import webserver.http.request.HttpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceResolverTest {

    @Test
    @DisplayName("존재하는 리소스 요청이 주어졌을 때, 200 OK 응답과 파일 내용이 전송되어야 한다.")
    void 올바른_리소스_요청_200_응답_반환_테스트() {
        // Given
        HttpRequest request = RequestBuilder.get("/index.html").build();
        ResourceResolver resolver = new ResourceResolver(request);

        // When
        ResolveResponse<byte[]> resolveResponse = resolver.resolve();

        // Then
        assertThat(resolveResponse.getStatusCode().toString()).contains("200 OK");
        assertThat(resolveResponse.getHeaders().get("Content-Type")).contains("text/html");
        assertThat(resolveResponse.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 리소스 요청이 주어졌을 때, 404 Not Found 예외가 발생해야 한다.")
    void 존재하지_않은_리소스_404응답_반환_테스트() {
        // Given
        HttpRequest request = RequestBuilder.get("/nonexistent.txt").build();
        ResourceResolver resolver = new ResourceResolver(request);

        // When & Then
        assertThatThrownBy(resolver::resolve)
                .isInstanceOf(HttpException.class)
                .hasMessageContaining("404 Not Found");
    }

    @Test
    @DisplayName("디렉토리 요청이 주어졌을 때, 해당 디렉토리 내의 index.html 파일을 반환해야 한다.")
    void 디렉토리_요청_테스트() {
        // Given
        HttpRequest request = RequestBuilder.get("/registration/index.html").build();
        ResourceResolver resolver = new ResourceResolver(request);

        // When
        ResolveResponse<byte[]> resolveResponse = resolver.resolve();

        // Then
        assertThat(resolveResponse.getStatusCode().toString()).contains("200 OK");
        assertThat(resolveResponse.getHeaders().get("Content-Type")).contains("text/html");
        assertThat(resolveResponse.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("디렉토리 요청이 주어졌을 때, 해당 디렉토리가 존재하지 않으면 404 Not Found 예외가 발생해야 한다.")
    void 존재하지_않는_디렉토리_요청_404응답_반환_테스트() {
        // Given
        HttpRequest request = RequestBuilder.get("/nonexistent_directory/index.html").build();
        ResourceResolver resolver = new ResourceResolver(request);

        // When & Then
        assertThatThrownBy(resolver::resolve)
                .isInstanceOf(HttpException.class)
                .hasMessageContaining("404 Not Found");
    }


}
