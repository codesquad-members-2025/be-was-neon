package handler.errorHandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static webserver.common.Constants.EMPTY;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

class NotFoundHandlerTest {

    @Test
    @DisplayName("존재하지 않는 요청에 대해 404 페이지와 함께 Not Found 상태를 반환한다.")
    void testHandle_Returns404Response() {
        // given
        byte[] expectedBody = "<html><body>404 Not Found</body></html>".getBytes(StandardCharsets.UTF_8);

        // mock ResourceLoader
        ResourceLoader resourceLoader = Mockito.mock(ResourceLoader.class);
        Mockito.when(resourceLoader.fileToBytes("/error/404.html")).thenReturn(expectedBody);

        NotFoundHandler handler = new NotFoundHandler(resourceLoader);

        // mock Request (내용은 크게 중요하지 않음)
        Request mockRequest = Mockito.mock(Request.class);

        // when
        Response response = handler.handle(mockRequest);

        // then
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(expectedBody);
        assertThat(response.getRedirectPath()).isEqualTo(EMPTY);
    }

}