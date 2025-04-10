package webserver.http.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.http.common.ContentType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class HttpResponseTest {

    private HttpResponse response;
    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() {
        this.baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        response = new HttpResponse(dos);
    }

    @ParameterizedTest(name = "[{index}] {0} with content type {1}")
    @CsvSource({
            "OK, JSON, 'Hello World'",
            "BAD_REQUEST, JSON, '{\"error\":\"Bad Request\"}'",
            "UNSUPPORTED_MEDIA_TYPE, JSON, '{\"error\":\"Unsupported Media Type\"}'"
    })
    @DisplayName("올바른 응답 정보가 주어졌을 때, HTTP 응답이 올바르게 전송되어야 한다.")
    public void 올바른_응답_테스트(String statusStr, String contentTypeStr, String body) throws IOException {
        // Given
        HttpStatusCode status = HttpStatusCode.valueOf(statusStr);
        ContentType contentType = ContentType.valueOf(contentTypeStr);

        // When
        response.sendResponse(status, contentType, body.getBytes());

        // Then
        String output = baos.toString();
        Assertions.assertThat(output).contains("HTTP/1.1 " + status.getStatusCode() + " " + status.getReasonPhrase());
        Assertions.assertThat(output).contains("Content-Type: " + contentType.getMimeType());
        Assertions.assertThat(output).contains("Content-Length: " + body.getBytes().length);
        Assertions.assertThat(output).endsWith(body);
    }

}
