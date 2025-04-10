package webserver.http.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.common.ContentType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class HttpResponseTest {

    private HttpResponse response;
    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() {
        this.baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        response = new HttpResponse(dos);
    }

    @Test
    @DisplayName("올바른 응답 정보가 주어졌을 때, 200 OK 응답과 올바른 헤더 및 본문이 전송되어야 한다.")
    public void 올바른_응답_200_응답_테스트() throws IOException {
        // Given
        String body = "Hello World";

        // When
        response.sendResponse(HttpStatusCode.OK, ContentType.JSON, body.getBytes());

        // Then
        String output = baos.toString(StandardCharsets.UTF_8);
        Assertions.assertThat(output).contains("HTTP/1.1 200 OK");
        Assertions.assertThat(output).contains("Content-Type: application/json");
        Assertions.assertThat(output).contains("Content-Length: " + body.getBytes().length);
        Assertions.assertThat(output).endsWith("Hello World");
    }

    @Test
    @DisplayName("send400() 호출 시, 400 Bad Request 응답이 전송되어야 한다.")
    public void 응답_400_테스트() throws IOException {
        // When
        response.sendResponse(HttpStatusCode.BAD_REQUEST, ContentType.JSON, "Bad Request".getBytes());

        // Then
        String output = baos.toString(StandardCharsets.UTF_8);
        Assertions.assertThat(output).contains("400 Bad Request");
    }

    @Test
    @DisplayName("send404() 호출 시, 404 Not Found 응답이 전송되어야 한다.")
    public void 응답_404_테스트() throws IOException {
        // When
        response.sendResponse(HttpStatusCode.NOT_FOUND, ContentType.JSON, "Not Found".getBytes());

        // Then
        String output = baos.toString();
        Assertions.assertThat(output).contains("404 Not Found");
    }

}
