package webserver.resolver;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.StringReader;

class ResourceResolverTest {

    @Test
    @DisplayName("존재하는 리소스 요청이 주어졌을 때, 200 OK 응답과 파일 내용이 전송되어야 한다.")
    public void 올바른_리소스_요청_200_응답_반환_테스트() throws Exception {
        // Given
        String requestStr =
                """
                        GET /index.html HTTP/1.1\r
                        Host: localhost\r
                        \r
                        """;
        BufferedReader br = new BufferedReader(new StringReader(requestStr));
        HttpRequest request = new HttpRequest(br);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        HttpResponse response = new HttpResponse(dos);

        // When
        ResourceResolver resolver = new ResourceResolver(request, response);
        resolver.resolve();

        // Then
        String output = baos.toString();
        Assertions.assertThat(output).contains("200 OK");
        Assertions.assertThat(output).contains("text/html");
    }

    @Test
    @DisplayName("존재하지 않는 리소스 요청이 주어졌을 때, 404 Not Found 응답이 전송되어야 한다.")
    public void 존재하지_않은_리소스_404응답_반환_테스트() throws Exception {
        // Given
        String requestStr =
                """
                        GET /nonexistent.txt HTTP/1.1\r
                        Host: localhost\r
                        \r
                        """;
        BufferedReader br = new BufferedReader(new StringReader(requestStr));
        HttpRequest request = new HttpRequest(br);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        HttpResponse response = new HttpResponse(dos);

        // When
        ResourceResolver resolver = new ResourceResolver(request, response);
        resolver.resolve();

        // Then
        String output = baos.toString();
        Assertions.assertThat(output).contains("404 Not Found");
    }

}
