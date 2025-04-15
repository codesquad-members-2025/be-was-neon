package webserver.http.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.common.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

class HttpResponseTest {

    @Test
    @DisplayName("HTTP 응답 바디가 올바르게 byte[]로 변환되어야 한다.")
    void 올바른_객체_생성_테스트() {
        // Given
        StatusLine statusLine = new StatusLine(HttpStatusCode.OK);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html");
        byte[] body = "<html><body>Hello, World!</body></html>".getBytes();

        // When
        HttpResponse response = new HttpResponse(statusLine, headers, body);

        // Then
        byte[] responseBytes = response.getBytes();
        String responseString = new String(responseBytes);

        assertThat(responseString).contains("HTTP/1.1 200 OK");
        assertThat(responseString).contains("Content-Type: text/html");
        assertThat(responseString).contains("<html><body>Hello, World!</body></html>");
    }

}
